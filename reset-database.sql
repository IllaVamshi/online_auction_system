-- Disable foreign key checks to avoid deletion errors
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate tables to delete all data and reset auto-increment (auction numbers, user numbers, etc)
TRUNCATE TABLE bids;
TRUNCATE TABLE auctions;
TRUNCATE TABLE users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- If you have any additional tables like roles, categories, etc., add TRUNCATE statements for them as well above.
