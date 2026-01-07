import React, { useContext } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { TrendingUp, Wallet, LogOut } from "lucide-react";

// Components
import { CandleChart } from "./components/Chart/Candlechart.jsx";
import Login from "./auth/Login";
import Register from "./auth/Register";
import TradePanel from "./trade/TradePanel";
import TransactionHistory from "./trade/TransactionHistory";

// Hooks & Context
import { useMarketData } from "./hooks/useMarketData";
// 1. CHANGE: Import Context instead of useAuth
import { AuthContext } from "./auth/authContext";
import { usePortfolio } from "./portfolio/PortfolioContext";

// --- COMPOSANT : LA PAGE TABLEAU DE BORD (DASHBOARD) ---
const Dashboard = () => {
  // 2. CHANGE: Use useContext to get user and logout
  const { user, logout } = useContext(AuthContext);
  const { balance, portfolioItems } = usePortfolio();
  const { currentCandle, history } = useMarketData();

  const currentPrice = currentCandle?.close || 0;
  const btcHolding =
    portfolioItems.find((item) => item.symbol === "BTC")?.quantity || 0;
  const btcValue = btcHolding * currentPrice;
  const totalNetWorth = balance + btcValue;
  const profitLoss = totalNetWorth - 10000;

  return (
    <div className="min-h-screen bg-[#0F1115] text-white flex flex-col font-sans">
      {/* HEADER FIXE */}
      <header className="h-16 bg-[#13151b] border-b border-gray-800 flex items-center justify-between px-6 sticky top-0 z-50">
        <div className="flex items-center gap-6">
          <div className="flex items-center gap-2 text-emerald-400 font-mono text-sm">
            <TrendingUp size={16} />
            <span>
              BTC: $
              {currentPrice.toLocaleString(undefined, {
                minimumFractionDigits: 2,
              })}
            </span>
          </div>
          <div className="hidden md:flex items-center gap-2 border-l border-gray-700 pl-6">
            <span className="text-gray-500 text-[10px] uppercase font-bold tracking-wider">
              Net Worth
            </span>
            <span
              className={`font-mono font-bold ${
                profitLoss >= 0 ? "text-emerald-400" : "text-rose-400"
              }`}
            >
              $
              {totalNetWorth.toLocaleString(undefined, {
                minimumFractionDigits: 2,
              })}
            </span>
          </div>
        </div>

        <div className="flex items-center gap-4">
          <div className="text-right hidden sm:block">
             <span className="block text-xs font-bold text-gray-200">
                {user?.username}
             </span>
             <span className="block text-[10px] text-gray-500 uppercase">Pro Account</span>
          </div>
          <button
            onClick={logout}
            className="p-2 text-gray-500 hover:text-rose-400 hover:bg-rose-500/10 rounded-lg transition-all"
          >
            <LogOut size={18} />
          </button>
        </div>
      </header>

      {/* CONTENU PRINCIPAL */}
      <main className="flex-1 p-6 grid grid-cols-1 md:grid-cols-12 gap-6 max-w-[1800px] mx-auto w-full">
        {/* COLONNE GAUCHE : GRAPHIQUE ET HISTORIQUE */}
        <section className="col-span-12 lg:col-span-9 flex flex-col gap-6">
          <div className="bg-[#13151b] rounded-xl p-1 border border-gray-800 h-[550px] relative shadow-2xl overflow-hidden">
            <div className="absolute top-5 left-5 z-10">
               <h2 className="text-2xl font-bold text-white tracking-tight">BTC/USD</h2>
               <div className="flex items-center gap-2 mt-1">
                 <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
                 <p className="text-[10px] text-gray-400 uppercase tracking-widest font-bold">Live Market</p>
               </div>
            </div>
            {/* Chart Component */}
            <div className="w-full h-full">
               <CandleChart data={history} stream={currentCandle} />
            </div>
          </div>
          <TransactionHistory />
        </section>

        {/* COLONNE DROITE : TRADING ET WALLET */}
        <section className="col-span-12 lg:col-span-3 flex flex-col gap-6">
          <TradePanel currentPrice={currentPrice} />

          <div className="bg-[#13151b] rounded-xl p-6 border border-gray-800 shadow-lg">
            <h3 className="text-sm font-bold mb-6 flex items-center gap-2 text-gray-400 uppercase tracking-wider">
              <Wallet size={16} className="text-emerald-500" /> Wallet Overview
            </h3>
            <div className="space-y-6">
              <div className="flex justify-between items-end border-b border-gray-800 pb-4">
                <span className="text-xs text-gray-500 font-medium">USD Balance</span>
                <span className="text-xl font-mono text-emerald-400 font-bold">
                  $
                  {balance.toLocaleString(undefined, {
                    minimumFractionDigits: 2,
                  })}
                </span>
              </div>
              <div className="flex justify-between items-end border-b border-gray-800 pb-4">
                <span className="text-xs text-gray-500 font-medium">BTC Holdings</span>
                <span className="text-xl font-mono text-blue-400 font-bold">
                  {btcHolding.toFixed(8)}
                </span>
              </div>
              <div className="bg-gray-900/50 p-4 rounded-lg border border-gray-800">
                <div className="text-[10px] text-gray-500 uppercase mb-1 font-bold">
                  P&L (All Time)
                </div>
                <div
                  className={`text-lg font-mono font-bold ${
                    profitLoss >= 0 ? "text-emerald-500" : "text-rose-500"
                  }`}
                >
                  {profitLoss >= 0 ? "+" : ""}$
                  {profitLoss.toLocaleString(undefined, {
                    minimumFractionDigits: 2,
                  })}
                </div>
              </div>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
};

// --- LOGIQUE DE ROUTAGE PRINCIPALE ---
function App() {
  // 3. CHANGE: Use useContext + derive isAuthenticated
  const { user, loading } = useContext(AuthContext);
  const isAuthenticated = !!user;

  if (loading) {
    return (
      <div className="min-h-screen bg-[#0F1115] flex items-center justify-center">
         <div className="flex flex-col items-center gap-4">
            <div className="w-12 h-12 border-4 border-emerald-500/30 border-t-emerald-500 rounded-full animate-spin"></div>
            <div className="text-emerald-500 font-mono text-sm uppercase tracking-widest animate-pulse">Initializing Terminal...</div>
         </div>
      </div>
    );
  }

  return (
    <Routes>
      {/* Redirection automatique de la racine */}
      <Route
        path="/"
        element={
          isAuthenticated ? (
            <Navigate to="/dashboard" />
          ) : (
            <Navigate to="/login" />
          )
        }
      />

      {/* Pages Publiques */}
      <Route
        path="/login"
        element={!isAuthenticated ? <Login /> : <Navigate to="/dashboard" />}
      />
      <Route
        path="/register"
        element={!isAuthenticated ? <Register /> : <Navigate to="/dashboard" />}
      />

      {/* Page Privée */}
      <Route
        path="/dashboard"
        element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" />}
      />

      {/* Redirection pour les URLs inexistantes */}
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
}

export default App;