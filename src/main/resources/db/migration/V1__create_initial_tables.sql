-- Create blogs table
CREATE TABLE IF NOT EXISTS blogs (
    id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(255) UNIQUE,
    slug VARCHAR(255) UNIQUE NOT NULL,
    title_vi TEXT,
    title_en TEXT,
    tags TEXT[],
    image VARCHAR(500),
    excerpt_vi TEXT,
    excerpt_en TEXT,
    content_vi_raw TEXT,
    content_vi_html TEXT,
    content_vi_text TEXT,
    content_en_raw TEXT,
    content_en_html TEXT,
    content_en_text TEXT,
    status VARCHAR(50) DEFAULT 'active',
    published_at TIMESTAMP WITH TIME ZONE,
    source VARCHAR(100),
    source_url VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create index for slug and status
CREATE INDEX idx_blogs_slug ON blogs(slug);
CREATE INDEX idx_blogs_status ON blogs(status);
CREATE INDEX idx_blogs_published_at ON blogs(published_at DESC);

-- Create bots table
CREATE TABLE IF NOT EXISTS bots (
    id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(255) UNIQUE,
    external_key VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    image VARCHAR(500),
    affiliate_link VARCHAR(500),
    name_vi VARCHAR(255),
    name_en VARCHAR(255),
    summary_vi TEXT,
    summary_en TEXT,
    tags TEXT[],
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create index for bot slug
CREATE INDEX idx_bots_slug ON bots(slug);
CREATE INDEX idx_bots_external_key ON bots(external_key);

-- Create bot_features table (one-to-many relationship)
CREATE TABLE IF NOT EXISTS bot_features (
    id BIGSERIAL PRIMARY KEY,
    bot_id BIGINT NOT NULL,
    content_vi TEXT,
    content_en TEXT,
    display_order INTEGER DEFAULT 0,
    CONSTRAINT fk_bot_features_bot FOREIGN KEY (bot_id) REFERENCES bots(id) ON DELETE CASCADE
);

CREATE INDEX idx_bot_features_bot_id ON bot_features(bot_id);

-- Create bot_strengths table (one-to-many relationship)
CREATE TABLE IF NOT EXISTS bot_strengths (
    id BIGSERIAL PRIMARY KEY,
    bot_id BIGINT NOT NULL,
    content_vi TEXT,
    content_en TEXT,
    display_order INTEGER DEFAULT 0,
    CONSTRAINT fk_bot_strengths_bot FOREIGN KEY (bot_id) REFERENCES bots(id) ON DELETE CASCADE
);

CREATE INDEX idx_bot_strengths_bot_id ON bot_strengths(bot_id);

-- Create bot_weaknesses table (one-to-many relationship)
CREATE TABLE IF NOT EXISTS bot_weaknesses (
    id BIGSERIAL PRIMARY KEY,
    bot_id BIGINT NOT NULL,
    content_vi TEXT,
    content_en TEXT,
    display_order INTEGER DEFAULT 0,
    CONSTRAINT fk_bot_weaknesses_bot FOREIGN KEY (bot_id) REFERENCES bots(id) ON DELETE CASCADE
);

CREATE INDEX idx_bot_weaknesses_bot_id ON bot_weaknesses(bot_id);

-- Create bot_target_users table (one-to-many relationship)
CREATE TABLE IF NOT EXISTS bot_target_users (
    id BIGSERIAL PRIMARY KEY,
    bot_id BIGINT NOT NULL,
    content_vi TEXT,
    content_en TEXT,
    display_order INTEGER DEFAULT 0,
    CONSTRAINT fk_bot_target_users_bot FOREIGN KEY (bot_id) REFERENCES bots(id) ON DELETE CASCADE
);

CREATE INDEX idx_bot_target_users_bot_id ON bot_target_users(bot_id);

-- Create bot_pricing table (one-to-many relationship)
CREATE TABLE IF NOT EXISTS bot_pricing (
    id BIGSERIAL PRIMARY KEY,
    bot_id BIGINT NOT NULL,
    plan_vi VARCHAR(255),
    plan_en VARCHAR(255),
    price_text_vi VARCHAR(255),
    price_text_en VARCHAR(255),
    amount DECIMAL(10,2),
    currency VARCHAR(10) DEFAULT 'USD',
    interval VARCHAR(50),
    display_order INTEGER DEFAULT 0,
    CONSTRAINT fk_bot_pricing_bot FOREIGN KEY (bot_id) REFERENCES bots(id) ON DELETE CASCADE
);

CREATE INDEX idx_bot_pricing_bot_id ON bot_pricing(bot_id);

-- Create update timestamp trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_blogs_updated_at BEFORE UPDATE ON blogs
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_bots_updated_at BEFORE UPDATE ON bots
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();