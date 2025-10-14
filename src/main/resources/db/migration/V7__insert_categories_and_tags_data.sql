-- Insert categories
INSERT INTO categories (id, title, description) VALUES
(1, 'Customer Service & Support', 'AI tools for customer service and support'),
(2, 'AI Education', 'AI tools for education and learning'),
(3, 'Office AI', 'AI tools for office productivity'),
(4, 'Growth & Marketing AI', 'AI tools for marketing and growth'),
(5, 'Writing & Editing AI', 'AI tools for writing and editing'),
(6, 'Technology & IT', 'AI tools for technology and IT'),
(7, 'Design & Creative AI', 'AI tools for design and creative work'),
(8, 'Workflow & Automation', 'AI tools for workflow and automation')
ON CONFLICT DO NOTHING;

-- Insert tags
INSERT INTO tags (name, category_id) VALUES
('AI Chatbot', 1),
('AI Assistant', 1),
('AI Language', 1),
('AI Presentation', 2),
('AI Education', 2),
('AI Productivity', 3),
('AI Document', 3),
('AI Notetaker', 3),
('AI SEO', 4),
('AI Social Media', 4),
('AI Ads', 4),
('AI Writing', 4),
('AI All In One', 6),
('AI Website Builder', 6),
('AI Detector', 6),
('AI Data', 6),
('AI Design', 7),
('AI Video', 7),
('AI Photo', 7),
('AI Thumbnail', 7),
('AI Music', 7),
('AI 3D Model', 7),
('AI Audio', 7),
('AI Workflow', 8)
ON CONFLICT (name) DO NOTHING;
