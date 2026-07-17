-- Drop the old unique index that did not account for soft-delete/active flags.
DROP INDEX IF EXISTS uk_resort_facility_groups_resort_platform;

-- Recreate it so that only active, non-deleted rows participate in the uniqueness check.
-- This allows the same (resort_id, facility_group_id) pair to be reused after a soft-delete.
CREATE UNIQUE INDEX uk_resort_facility_groups_resort_platform
    ON resort_facility_groups (resort_id, facility_group_id)
    WHERE facility_group_id IS NOT NULL
      AND is_active = true
      AND is_deleted = false;
