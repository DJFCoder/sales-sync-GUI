-- Main initialization script for SalesSync database
-- This script will run all the necessary scripts in the correct order

-- First create the schema
SOURCE schema.sql;

-- Then insert initial data
SOURCE data.sql;

-- Create indexes for better performance
SOURCE indexes.sql;

-- Finally create views
SOURCE views.sql;

COMMIT;

-- cd /sales-sync/src/main/resources/db
-- mysql -u root-p < init.sql