-- Create tables for ZeroAds

-- 1. Stats Table
CREATE TABLE stats (
  id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  blocked INTEGER DEFAULT 0,
  data_saved TEXT DEFAULT '0 MB',
  trackers INTEGER DEFAULT 0,
  uptime TEXT DEFAULT '0h 0m',
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Insert initial stats row
INSERT INTO stats (blocked, data_saved, trackers, uptime)
VALUES (0, '0 MB', 0, '0h 0m');

-- 2. Blocked Requests Table (Live Traffic)
CREATE TABLE blocked_requests (
  id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  domain TEXT NOT NULL,
  type TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Enable Realtime for these tables
ALTER PUBLICATION supabase_realtime ADD TABLE stats;
ALTER PUBLICATION supabase_realtime ADD TABLE blocked_requests;

-- RLS Policies (Allow all for demo purposes, harden in production)
ALTER TABLE stats ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Public Access" ON stats FOR SELECT USING (true);
CREATE POLICY "Public Update" ON stats FOR UPDATE USING (true);

ALTER TABLE blocked_requests ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Public Access" ON blocked_requests FOR SELECT USING (true);
CREATE POLICY "Public Insert" ON blocked_requests FOR INSERT WITH CHECK (true);
