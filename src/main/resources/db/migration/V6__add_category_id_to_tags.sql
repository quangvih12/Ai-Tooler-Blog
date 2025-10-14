-- Add category_id column to tags table
ALTER TABLE tags ADD COLUMN category_id BIGINT;

-- Add foreign key constraint to reference categories table
ALTER TABLE tags ADD CONSTRAINT fk_tags_category
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL;

-- Add index for better performance
CREATE INDEX idx_tags_category_id ON tags(category_id);