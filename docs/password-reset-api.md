# Password Reset API

Base URL: `/api/v1/auth`

Covers the forgot-password flow every customer relies on to set a real password: a booker's POS booking
(`POST /api/v1/resorts/{resort-id}/bookings/pos`) auto-registers the customer with a random password that is
**never sent anywhere** — instead the customer gets a notification pointing them at the (placeholder,
env-configured) customer portal and telling them to use "forgot password." This module is that flow: a 6-digit
OTP is generated and delivered to whichever channel the customer's username resolves to — email, or WhatsApp
when the matching phone number is WhatsApp-reachable — then redeemed for a real password.

Both endpoints are public (`/api/v1/auth/**` is permit-all) and are not gated behind `Accept-Language`.

---

## Endpoints

| Method | Path                          | Description                          |
|--------|-------------------------------|---------------------------------------|
| POST   | `/api/v1/auth/forgot-password` | Request an OTP for a username         |
| POST   | `/api/v1/auth/reset-password`  | Redeem an OTP and set a new password  |

---

## Delivery channel

- If the username is an email address, the OTP is sent via the platform mail config registered under
  `MailProviderConfigCode.PASSWORD_RESET_EMAIL_NOTIFICATIONS`. If no such config exists, the request fails
  (`409 CONFLICT`) rather than silently doing nothing — unlike the booking flow's best-effort welcome email,
  there's no other way for the customer to get the code.
- If the username is a phone number, the OTP is sent over WhatsApp **only if** that phone's `is_whatsapp` flag
  is true (see `user_phones`). There is no SMS fallback; a non-WhatsApp phone fails the request the same way.
- WhatsApp sending is currently a log-only stub (`WhatsAppSendService`) pending a real provider integration
  (Twilio / Meta Cloud API) — messages are logged, not actually delivered, until that lands.

## OTP lifetime

The code is 6 digits and expires after `jwt.otp.expiration-minutes` (env var `OTP_EXPIRATION_MINUTES`) from
generation. A code can only be redeemed once — `reset-password` marks it used, so a stale/replayed code fails
even before expiry.

---

## POST `/api/v1/auth/forgot-password`

Generates a fresh OTP for the given username and delivers it through email or WhatsApp.

### Request Fields

| Field      | Type   | Required | Description                          |
|------------|--------|----------|----------------------------------------|
| `username` | String | Yes      | The account's username (email or phone) |

```json
{
  "username": "customer@example.com"
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 42
}
```

`id` is the internal id of the generated OTP record — not the code itself, which is only ever sent through the
delivery channel, never returned in the API response.

---

## POST `/api/v1/auth/reset-password`

Verifies the OTP and sets a new password for the account.

### Request Fields

| Field                 | Type   | Required | Description                                   |
|-----------------------|--------|----------|------------------------------------------------|
| `username`            | String | Yes      | The account's username (email or phone)         |
| `otp`                 | String | Yes      | The 6-digit code from `forgot-password`         |
| `new_password`        | String | Yes      | The password to set                             |
| `confirm_new_password`| String | Yes      | Must match `new_password`                       |

```json
{
  "username": "customer@example.com",
  "otp": "482913",
  "new_password": "NewSecurePassword123",
  "confirm_new_password": "NewSecurePassword123"
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 17
}
```

`id` is the user's id.

---

## Error Responses

| HTTP Status | Error Code           | Cause                                                                 |
|-------------|-----------------------|------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`    | `new_password`/`confirm_new_password` mismatch, or an invalid/expired/already-used OTP |
| 404         | `ENTITY_NOT_FOUND`    | No user exists for the given username                                  |
| 409         | `CONFLICT`            | No delivery channel available (no mail config for the code, or the phone isn't WhatsApp-reachable) |
