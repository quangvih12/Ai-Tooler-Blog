#!/usr/bin/env python3
import json
import re
from datetime import datetime

def escape_sql_string(value):
    """Escape single quotes and handle None values for SQL"""
    if value is None:
        return 'NULL'
    return "'" + str(value).replace("'", "''") + "'"

def convert_mongodb_date(date_obj):
    """Convert MongoDB date format to PostgreSQL timestamp"""
    if isinstance(date_obj, dict) and '$date' in date_obj:
        date_str = date_obj['$date']
        # Handle different date formats
        try:
            # Try ISO format first
            dt = datetime.fromisoformat(date_str.replace('Z', '+00:00'))
            return dt.strftime('%Y-%m-%d %H:%M:%S')
        except:
            # Try other formats if needed
            return datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    return None

def array_to_postgres(arr):
    """Convert Python array to PostgreSQL array format"""
    if not arr:
        return 'ARRAY[]::text[]'
    escaped_items = [str(item).replace("'", "''") for item in arr]
    return "ARRAY[" + ",".join([f"'{item}'" for item in escaped_items]) + "]"

def parse_price_amount(price_text):
    """Extract numeric amount from price text"""
    if not price_text:
        return None
    # Extract numbers from price text like "$11/month"
    match = re.search(r'[\d.]+', price_text)
    return float(match.group()) if match else None

def convert_blogs_to_sql(blogs_file, output_file):
    """Convert blogs.json to SQL insert statements"""
    with open(blogs_file, 'r', encoding='utf-8') as f:
        blogs = json.load(f)
    
    with open(output_file, 'w', encoding='utf-8') as out:
        out.write("-- Blog data migration\n")
        out.write("-- Generated from blogs.json\n\n")
        
        for blog in blogs:
            external_id = blog['_id']['$oid'] if '_id' in blog and '$oid' in blog['_id'] else None
            
            # Extract multilingual content
            title_vi = blog.get('title', {}).get('vi')
            title_en = blog.get('title', {}).get('en')
            excerpt_vi = blog.get('excerpt', {}).get('vi')
            excerpt_en = blog.get('excerpt', {}).get('en')
            
            # Extract content in different formats
            content_vi = blog.get('content', {}).get('vi', {})
            content_en = blog.get('content', {}).get('en', {})
            
            content_vi_raw = content_vi.get('raw')
            content_vi_html = content_vi.get('html')
            content_vi_text = content_vi.get('text')
            content_en_raw = content_en.get('raw')
            content_en_html = content_en.get('html')
            content_en_text = content_en.get('text')
            
            # Convert dates
            published_at = convert_mongodb_date(blog.get('publishedAt'))
            created_at = convert_mongodb_date(blog.get('createdAt'))
            updated_at = convert_mongodb_date(blog.get('updatedAt'))
            
            # Build INSERT statement
            out.write(f"""
INSERT INTO blogs (
    external_id, slug, title_vi, title_en, tags, image, 
    excerpt_vi, excerpt_en, content_vi_raw, content_vi_html, 
    content_vi_text, content_en_raw, content_en_html, content_en_text,
    status, published_at, source, source_url, created_at, updated_at
) VALUES (
    {escape_sql_string(external_id)},
    {escape_sql_string(blog.get('slug'))},
    {escape_sql_string(title_vi)},
    {escape_sql_string(title_en)},
    {array_to_postgres(blog.get('tags', []))},
    {escape_sql_string(blog.get('image'))},
    {escape_sql_string(excerpt_vi)},
    {escape_sql_string(excerpt_en)},
    {escape_sql_string(content_vi_raw)},
    {escape_sql_string(content_vi_html)},
    {escape_sql_string(content_vi_text)},
    {escape_sql_string(content_en_raw)},
    {escape_sql_string(content_en_html)},
    {escape_sql_string(content_en_text)},
    {escape_sql_string(blog.get('status', 'active'))},
    {escape_sql_string(published_at) if published_at else 'NULL'},
    {escape_sql_string(blog.get('source'))},
    {escape_sql_string(blog.get('sourceUrl'))},
    {escape_sql_string(created_at) if created_at else 'CURRENT_TIMESTAMP'},
    {escape_sql_string(updated_at) if updated_at else 'CURRENT_TIMESTAMP'}
);
""")

def convert_bots_to_sql(bots_file, output_file):
    """Convert bots.json to SQL insert statements"""
    with open(bots_file, 'r', encoding='utf-8') as f:
        bots = json.load(f)
    
    with open(output_file, 'a', encoding='utf-8') as out:
        out.write("\n\n-- Bot data migration\n")
        out.write("-- Generated from bots.json\n\n")
        
        for bot in bots:
            external_id = bot['_id']['$oid'] if '_id' in bot and '$oid' in bot['_id'] else None
            
            # Extract multilingual content
            name_vi = bot.get('name', {}).get('vi')
            name_en = bot.get('name', {}).get('en')
            summary_vi = bot.get('summary', {}).get('vi')
            summary_en = bot.get('summary', {}).get('en')
            
            # Insert bot main record
            out.write(f"""
INSERT INTO bots (
    external_id, external_key, slug, image, affiliate_link,
    name_vi, name_en, summary_vi, summary_en, tags
) VALUES (
    {escape_sql_string(external_id)},
    {escape_sql_string(bot.get('externalKey'))},
    {escape_sql_string(bot.get('slug'))},
    {escape_sql_string(bot.get('image'))},
    {escape_sql_string(bot.get('affiliateLink'))},
    {escape_sql_string(name_vi)},
    {escape_sql_string(name_en)},
    {escape_sql_string(summary_vi)},
    {escape_sql_string(summary_en)},
    {array_to_postgres(bot.get('tags', []))}
) RETURNING id;

-- Get the last inserted bot id
DO $$
DECLARE
    bot_id BIGINT;
BEGIN
    bot_id := lastval();
""")
            
            # Insert features
            features = bot.get('features', [])
            for idx, feature in enumerate(features):
                content_vi = feature.get('vi')
                content_en = feature.get('en')
                out.write(f"""
    INSERT INTO bot_features (bot_id, content_vi, content_en, display_order)
    VALUES (bot_id, {escape_sql_string(content_vi)}, {escape_sql_string(content_en)}, {idx});
""")
            
            # Insert strengths
            strengths = bot.get('strengths', [])
            for idx, strength in enumerate(strengths):
                content_vi = strength.get('vi')
                content_en = strength.get('en')
                out.write(f"""
    INSERT INTO bot_strengths (bot_id, content_vi, content_en, display_order)
    VALUES (bot_id, {escape_sql_string(content_vi)}, {escape_sql_string(content_en)}, {idx});
""")
            
            # Insert weaknesses
            weaknesses = bot.get('weaknesses', [])
            for idx, weakness in enumerate(weaknesses):
                content_vi = weakness.get('vi')
                content_en = weakness.get('en')
                out.write(f"""
    INSERT INTO bot_weaknesses (bot_id, content_vi, content_en, display_order)
    VALUES (bot_id, {escape_sql_string(content_vi)}, {escape_sql_string(content_en)}, {idx});
""")
            
            # Insert target users
            target_users = bot.get('targetUsers', [])
            for idx, target_user in enumerate(target_users):
                content_vi = target_user.get('vi')
                content_en = target_user.get('en')
                out.write(f"""
    INSERT INTO bot_target_users (bot_id, content_vi, content_en, display_order)
    VALUES (bot_id, {escape_sql_string(content_vi)}, {escape_sql_string(content_en)}, {idx});
""")
            
            # Insert pricing plans
            pricing_plans = bot.get('pricing', [])
            for idx, pricing in enumerate(pricing_plans):
                plan = pricing.get('plan', {})
                price_text = pricing.get('priceText', {})
                amount = parse_price_amount(price_text.get('en'))
                
                out.write(f"""
    INSERT INTO bot_pricing (bot_id, plan_vi, plan_en, price_text_vi, price_text_en, 
                            amount, currency, interval, display_order)
    VALUES (bot_id, {escape_sql_string(plan.get('vi'))}, {escape_sql_string(plan.get('en'))},
            {escape_sql_string(price_text.get('vi'))}, {escape_sql_string(price_text.get('en'))},
            {amount if amount else 'NULL'}, {escape_sql_string(pricing.get('currency', 'USD'))},
            {escape_sql_string(pricing.get('interval'))}, {idx});
""")
            
            out.write("""
END $$;
""")

if __name__ == "__main__":
    # Convert both JSON files to SQL
    output_file = "V2__import_data.sql"
    
    print("Converting blogs.json to SQL...")
    convert_blogs_to_sql("../../../../blogs.json", output_file)
    
    print("Converting bots.json to SQL...")
    convert_bots_to_sql("../../../../bots.json", output_file)
    
    print(f"SQL migration file created: {output_file}")
    print("Copy this file to src/main/resources/db/migration/ to run the migration")