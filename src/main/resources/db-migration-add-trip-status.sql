-- Adds the trip lifecycle status. Existing trips start as AGENDADA so each
-- historical record can be reviewed and explicitly marked as completed/cancelled.
ALTER TABLE viagens
    ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'AGENDADA';
