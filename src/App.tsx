/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import React, { useState, useEffect } from 'react';
import { 
  Shield, 
  ShieldCheck, 
  ShieldAlert, 
  Activity, 
  Settings, 
  BarChart3, 
  Smartphone, 
  Globe, 
  Zap,
  Lock,
  ChevronRight,
  Download,
  LogOut,
  User,
  Mail,
  Key
} from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';
import { supabase } from './lib/supabase';

const CyberButton = ({ children, onClick, active = false, className = "", type = "button", disabled = false }: any) => (
  <button 
    type={type}
    onClick={onClick}
    disabled={disabled}
    className={`relative px-6 py-3 font-mono text-sm uppercase tracking-widest transition-all duration-300 overflow-hidden group disabled:opacity-50 ${
      active 
        ? "bg-cyan-500 text-black shadow-[0_0_20px_rgba(6,182,212,0.5)]" 
        : "bg-zinc-900 text-cyan-500 border border-cyan-500/30 hover:border-cyan-500 hover:shadow-[0_0_15px_rgba(6,182,212,0.2)]"
    } ${className}`}
  >
    <div className="relative z-10 flex items-center gap-2 justify-center">
      {children}
    </div>
    <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/10 to-transparent -translate-x-full group-hover:translate-x-full transition-transform duration-1000" />
  </button>
);

const StatCard = ({ label, value, icon: Icon, color = "cyan" }: any) => (
  <div className="bg-zinc-900/50 border border-zinc-800 p-6 rounded-2xl relative overflow-hidden group">
    <div className={`absolute top-0 right-0 p-4 opacity-10 group-hover:opacity-20 transition-opacity text-${color}-500`}>
      <Icon size={80} />
    </div>
    <div className="relative z-10">
      <p className="text-zinc-500 text-xs uppercase tracking-tighter mb-1 font-mono">{label}</p>
      <h3 className={`text-3xl font-bold text-${color}-400 font-mono`}>{value}</h3>
    </div>
    <div className={`absolute bottom-0 left-0 h-1 bg-${color}-500 transition-all duration-500 w-0 group-hover:w-full`} />
  </div>
);

const AuthScreen = ({ onAuthSuccess }: { onAuthSuccess: () => void }) => {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleAuth = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      if (isLogin) {
        const { error } = await supabase.auth.signInWithPassword({ email, password });
        if (error) throw error;
      } else {
        const { error } = await supabase.auth.signUp({ email, password });
        if (error) throw error;
        alert('Check your email for the confirmation link!');
      }
      onAuthSuccess();
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-6 relative">
      <div className="absolute inset-0 bg-[linear-gradient(to_right,#164e63_1px,transparent_1px),linear-gradient(to_bottom,#164e63_1px,transparent_1px)] bg-[size:4rem_4rem] [mask-image:radial-gradient(ellipse_60%_50%_at_50%_0%,#000_70%,transparent_100%)] pointer-events-none" />
      
      <motion.div 
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="w-full max-w-md bg-zinc-950/80 border border-cyan-500/20 rounded-3xl p-8 backdrop-blur-2xl relative z-10 shadow-[0_0_50px_rgba(6,182,212,0.1)]"
      >
        <div className="text-center mb-8">
          <div className="w-16 h-16 bg-cyan-500 rounded-2xl flex items-center justify-center mx-auto mb-4 shadow-[0_0_30px_rgba(6,182,212,0.3)]">
            <Shield className="text-black" size={32} />
          </div>
          <h1 className="text-2xl font-bold font-mono tracking-tighter uppercase text-cyan-50">
            {isLogin ? "Access Terminal" : "Initialize Account"}
          </h1>
          <p className="text-cyan-500/50 text-[10px] uppercase tracking-[0.3em] mt-1 font-mono">ZeroAds Security Labs</p>
        </div>

        <form onSubmit={handleAuth} className="space-y-6">
          <div className="space-y-2">
            <label className="text-[10px] uppercase tracking-widest text-zinc-500 font-mono ml-1">Email Address</label>
            <div className="relative">
              <Mail className="absolute left-4 top-1/2 -translate-y-1/2 text-cyan-500/30" size={18} />
              <input 
                type="email" 
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full bg-black border border-zinc-800 rounded-xl py-3 pl-12 pr-4 text-sm focus:border-cyan-500 outline-none transition-colors font-mono text-cyan-50 placeholder:text-zinc-700"
                placeholder="operator@zeroads.io"
              />
            </div>
          </div>

          <div className="space-y-2">
            <label className="text-[10px] uppercase tracking-widest text-zinc-500 font-mono ml-1">Access Key</label>
            <div className="relative">
              <Key className="absolute left-4 top-1/2 -translate-y-1/2 text-cyan-500/30" size={18} />
              <input 
                type="password" 
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full bg-black border border-zinc-800 rounded-xl py-3 pl-12 pr-4 text-sm focus:border-cyan-500 outline-none transition-colors font-mono text-cyan-50 placeholder:text-zinc-700"
                placeholder="••••••••"
              />
            </div>
          </div>

          {error && (
            <motion.div 
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="p-3 bg-red-500/10 border border-red-500/20 rounded-xl text-red-500 text-[10px] uppercase tracking-widest text-center font-mono"
            >
              Error: {error}
            </motion.div>
          )}

          <CyberButton type="submit" className="w-full py-4" active disabled={loading}>
            {loading ? "Processing..." : (isLogin ? "Login" : "Register")}
          </CyberButton>
        </form>

        <div className="mt-8 text-center">
          <button 
            onClick={() => setIsLogin(!isLogin)}
            className="text-[10px] uppercase tracking-widest text-zinc-500 hover:text-cyan-500 transition-colors font-mono"
          >
            {isLogin ? "Request New Credentials" : "Return to Login Terminal"}
          </button>
        </div>
      </motion.div>
    </div>
  );
};

export default function App() {
  const [session, setSession] = useState<any>(null);
  const [isProtected, setIsProtected] = useState(true);
  const [stats, setStats] = useState({
    blocked: 0,
    dataSaved: "0 MB",
    trackers: 0,
    uptime: "0h 0m"
  });
  const [liveTraffic, setLiveTraffic] = useState<any[]>([]);

  useEffect(() => {
    // Check current session
    supabase.auth.getSession().then(({ data: { session } }) => {
      setSession(session);
    });

    // Listen for auth changes
    const { data: { subscription } } = supabase.auth.onAuthStateChange((_event, session) => {
      setSession(session);
    });

    return () => subscription.unsubscribe();
  }, []);

  useEffect(() => {
    if (!session) return;

    // Initial fetch
    const fetchInitialData = async () => {
      const { data: statsData } = await supabase.from('stats').select('*').single();
      if (statsData) setStats(statsData);

      const { data: trafficData } = await supabase
        .from('blocked_requests')
        .select('*')
        .order('created_at', { ascending: false })
        .limit(5);
      if (trafficData) setLiveTraffic(trafficData);
    };

    fetchInitialData();

    // Real-time subscriptions
    const statsSubscription = supabase
      .channel('stats-changes')
      .on('postgres_changes', { event: 'UPDATE', schema: 'public', table: 'stats' }, (payload) => {
        setStats(payload.new as any);
      })
      .subscribe();

    const trafficSubscription = supabase
      .channel('traffic-changes')
      .on('postgres_changes', { event: 'INSERT', schema: 'public', table: 'blocked_requests' }, (payload) => {
        setLiveTraffic(prev => [payload.new, ...prev].slice(0, 5));
      })
      .subscribe();

    return () => {
      statsSubscription.unsubscribe();
      trafficSubscription.unsubscribe();
    };
  }, [session]);

  const handleLogout = async () => {
    await supabase.auth.signOut();
  };

  if (!session) {
    return <AuthScreen onAuthSuccess={() => {}} />;
  }

  return (
    <div className="min-h-screen bg-black text-zinc-100 font-sans selection:bg-emerald-500 selection:text-black">
      {/* Background Grid Effect */}
      <div className="fixed inset-0 bg-[linear-gradient(to_right,#18181b_1px,transparent_1px),linear-gradient(to_bottom,#18181b_1px,transparent_1px)] bg-[size:4rem_4rem] [mask-image:radial-gradient(ellipse_60%_50%_at_50%_0%,#000_70%,transparent_100%)] pointer-events-none" />

      <nav className="relative z-10 border-b border-zinc-800 bg-black/50 backdrop-blur-xl">
        <div className="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-cyan-500 rounded-lg flex items-center justify-center shadow-[0_0_20px_rgba(6,182,212,0.4)]">
              <Shield className="text-black" size={24} />
            </div>
            <div>
              <h1 className="text-xl font-bold tracking-tighter font-mono">ZERO<span className="text-cyan-500">ADS</span></h1>
              <p className="text-[10px] text-zinc-500 uppercase tracking-[0.2em] -mt-1">System-Wide Protection</p>
            </div>
          </div>
          
          <div className="hidden md:flex items-center gap-8 text-sm font-mono uppercase tracking-widest text-zinc-400">
            <a href="#" className="hover:text-cyan-500 transition-colors">Dashboard</a>
            <a href="#" className="hover:text-cyan-500 transition-colors">App Control</a>
            <a href="#" className="hover:text-cyan-500 transition-colors">DNS Settings</a>
            <a href="#" className="hover:text-cyan-500 transition-colors">Stats</a>
          </div>

          <div className="flex items-center gap-4">
            <div className="hidden lg:flex items-center gap-2 px-4 py-2 bg-zinc-900 border border-zinc-800 rounded-xl">
              <User size={14} className="text-cyan-500" />
              <span className="text-[10px] font-mono text-zinc-400 truncate max-w-[120px]">{session.user.email}</span>
            </div>
            <button 
              onClick={handleLogout}
              className="p-2 text-zinc-500 hover:text-red-500 transition-colors"
              title="Logout"
            >
              <LogOut size={20} />
            </button>
          </div>
        </div>
      </nav>

      <main className="relative z-10 max-w-7xl mx-auto px-6 py-12">
        <div className="grid lg:grid-cols-12 gap-12">
          
          {/* Left Column: Protection Status */}
          <div className="lg:col-span-5 space-y-8">
            <div className="bg-zinc-900/30 border border-zinc-800 rounded-3xl p-12 text-center relative overflow-hidden">
              <div className="absolute inset-0 bg-cyan-500/5 blur-[100px] pointer-events-none" />
              
              <motion.div 
                animate={{ 
                  scale: isProtected ? [1, 1.05, 1] : 1,
                  rotate: isProtected ? [0, 5, -5, 0] : 0
                }}
                transition={{ duration: 4, repeat: Infinity }}
                className="relative inline-block mb-8"
              >
                <div className={`w-48 h-48 rounded-full border-2 flex items-center justify-center transition-colors duration-500 ${
                  isProtected ? "border-cyan-500 shadow-[0_0_50px_rgba(6,182,212,0.2)]" : "border-zinc-700"
                }`}>
                  {isProtected ? (
                    <ShieldCheck size={80} className="text-cyan-500" />
                  ) : (
                    <ShieldAlert size={80} className="text-zinc-600" />
                  )}
                </div>
                {isProtected && (
                  <motion.div 
                    initial={{ scale: 0 }}
                    animate={{ scale: 1 }}
                    className="absolute -top-2 -right-2 w-12 h-12 bg-cyan-500 rounded-full flex items-center justify-center border-4 border-black"
                  >
                    <Zap size={20} className="text-black" />
                  </motion.div>
                )}
              </motion.div>

              <h2 className="text-4xl font-bold mb-2 font-mono">
                {isProtected ? "PROTECTION ACTIVE" : "PROTECTION PAUSED"}
              </h2>
              <p className="text-zinc-500 mb-8 max-w-xs mx-auto text-sm">
                {isProtected 
                  ? "Your device is currently shielded from ads, trackers, and malicious domains." 
                  : "Enable protection to block system-wide advertisements and tracking requests."}
              </p>

              <CyberButton 
                active={isProtected} 
                onClick={() => setIsProtected(!isProtected)}
                className="w-full py-5 text-lg"
              >
                {isProtected ? "STOP PROTECTION" : "START PROTECTION"}
              </CyberButton>
            </div>

            <div className="bg-zinc-900/30 border border-zinc-800 rounded-3xl p-8">
              <div className="flex items-center justify-between mb-6">
                <h3 className="font-mono text-sm uppercase tracking-widest text-zinc-400 flex items-center gap-2">
                  <Activity size={16} className="text-cyan-500" />
                  Live Traffic
                </h3>
                <span className="text-[10px] bg-cyan-500/10 text-cyan-500 px-2 py-1 rounded font-mono">REAL-TIME</span>
              </div>
              <div className="space-y-3">
                {liveTraffic.length > 0 ? liveTraffic.map((item, i) => (
                  <motion.div 
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    key={item.id || i} 
                    className="flex items-center justify-between p-3 bg-black/40 rounded-xl border border-zinc-800/50 text-xs font-mono"
                  >
                    <div className="flex items-center gap-3">
                      <div className="w-2 h-2 rounded-full bg-cyan-500 animate-pulse" />
                      <span className="text-zinc-300 truncate max-w-[150px]">{item.domain}</span>
                    </div>
                    <div className="flex items-center gap-4">
                      <span className="text-zinc-600">{item.type}</span>
                      <span className="text-cyan-500/80">Blocked</span>
                    </div>
                  </motion.div>
                )) : (
                  <p className="text-center text-zinc-600 font-mono text-xs py-4">Waiting for traffic...</p>
                )}
              </div>
            </div>
          </div>

          {/* Right Column: Stats & Features */}
          <div className="lg:col-span-7 space-y-8">
            <div className="grid sm:grid-cols-2 gap-4">
              <StatCard label="Ads Blocked" value={stats.blocked.toLocaleString()} icon={Shield} />
              <StatCard label="Data Saved" value={stats.dataSaved} icon={Zap} color="blue" />
              <StatCard label="Trackers Stopped" value={stats.trackers} icon={Lock} color="purple" />
              <StatCard label="Active Uptime" value={stats.uptime} icon={Activity} color="orange" />
            </div>

            <div className="bg-zinc-900/30 border border-zinc-800 rounded-3xl p-8">
              <h3 className="font-mono text-sm uppercase tracking-widest text-zinc-400 mb-8 flex items-center gap-2">
                <Settings size={16} className="text-cyan-500" />
                Advanced Engine Settings
              </h3>
              
              <div className="grid sm:grid-cols-2 gap-6">
                {[
                  { title: "Smart DNS Filtering", desc: "Uses Cloudflare & Google DNS with local overrides.", icon: Globe },
                  { title: "App-Level Control", desc: "Whitelist specific apps from ad blocking rules.", icon: Smartphone },
                  { title: "AI Tracker Detection", desc: "Heuristic analysis for zero-day tracking scripts.", icon: Zap },
                  { title: "Malware Protection", desc: "Blocks known phishing and cryptomining domains.", icon: ShieldAlert },
                ].map((feature, i) => (
                  <div key={i} className="group p-6 bg-black/40 border border-zinc-800 rounded-2xl hover:border-cyan-500/50 transition-all cursor-pointer">
                    <div className="w-10 h-10 bg-zinc-800 rounded-lg flex items-center justify-center mb-4 group-hover:bg-cyan-500 group-hover:text-black transition-colors">
                      <feature.icon size={20} />
                    </div>
                    <h4 className="font-bold mb-1 text-zinc-200">{feature.title}</h4>
                    <p className="text-xs text-zinc-500 leading-relaxed">{feature.desc}</p>
                  </div>
                ))}
              </div>
            </div>

            <div className="bg-cyan-500/5 border border-cyan-500/20 rounded-3xl p-8 flex items-center justify-between group cursor-pointer hover:bg-cyan-500/10 transition-all">
              <div className="flex items-center gap-6">
                <div className="w-16 h-16 bg-cyan-500/20 rounded-2xl flex items-center justify-center text-cyan-500">
                  <BarChart3 size={32} />
                </div>
                <div>
                  <h4 className="text-xl font-bold text-cyan-400 font-mono">DETAILED ANALYTICS</h4>
                  <p className="text-sm text-zinc-500">View weekly reports and data usage breakdown.</p>
                </div>
              </div>
              <ChevronRight className="text-cyan-500 group-hover:translate-x-2 transition-transform" />
            </div>
          </div>

        </div>
      </main>

      <footer className="relative z-10 border-t border-zinc-800 py-12 mt-12 bg-black/50">
        <div className="max-w-7xl mx-auto px-6 flex flex-col md:flex-row items-center justify-between gap-8">
          <div className="flex items-center gap-3 opacity-50">
            <Shield size={20} />
            <span className="font-mono text-xs uppercase tracking-widest">ZeroAds v1.0.4 Build 882</span>
          </div>
          <div className="flex gap-8 text-xs font-mono uppercase tracking-widest text-zinc-500">
            <a href="#" className="hover:text-emerald-500 transition-colors">Privacy Policy</a>
            <a href="#" className="hover:text-emerald-500 transition-colors">Documentation</a>
            <a href="#" className="hover:text-emerald-500 transition-colors">Github</a>
          </div>
          <p className="text-zinc-600 text-[10px] uppercase tracking-widest">© 2026 ZEROADS SECURITY LABS</p>
        </div>
      </footer>
    </div>
  );
}
