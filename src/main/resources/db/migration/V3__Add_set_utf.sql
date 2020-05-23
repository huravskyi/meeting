ALTER TABLE
    chat
    CHANGE last_message last_message
    VARCHAR(2040)
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;