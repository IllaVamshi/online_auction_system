-- SQL Script to Clear All Auction Data
-- Run this in your MySQL database to delete all users, items, and bids
-- WARNING: This will permanently delete all data!

-- Disable foreign key checks to avoid constraint errors
SET FOREIGN_KEY_CHECKS = 0;

-- Delete bids first (references items and users)
DELETE FROM bids;

-- Delete items (references users)
DELETE FROM items;

-- Delete users last
DELETE FROM users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Optional: Reset auto-increment counters
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE items AUTO_INCREMENT = 1;
ALTER TABLE bids AUTO_INCREMENT = 1;