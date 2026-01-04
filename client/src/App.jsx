import React from "react";
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
import { useAuth } from "./auth/authContext";
import { usePortfolio } from "./portfolio/PortfolioContext";

// --- COMPOSANT : LA PAGE TABLEAU DE BORD (DASHBOARD) ---
const Dashboard = () => {
  const { user, logout } = useAuth();
  const { balance, portfolioItems } = usePortfolio();
  const { currentCandle, history } = useMarketData();

  const currentPrice = currentCandle?.close || 0;
  const btcHolding =
    portfolioItems.find((item) => item.symbol === "BTC")?.quantity || 0;
  const btcValue = btcHolding * currentPrice;
  const totalNetWorth = balance + btcValue;
  const profitLoss = totalNetWorth - 10000;

  return (
    <div className="min-h-screen bg-crypto-dark text-white flex flex-col">
      {/* HEADER FIXE */}
      <header className="h-14 bg-black border-b border-gray-800 flex items-center justify-between px-6 sticky top-0 z-50">
        <div className="flex items-center gap-6">
          <div className="flex items-center gap-2 text-green-400 font-mono text-sm">
            <TrendingUp size={16} />
            <span>
              BTC: $
              {currentPrice.toLocaleString(undefined, {
                minimumFractionDigits: 2,
              })}
            </span>
          </div>
          <div className="hidden md:flex items-center gap-2 border-l border-gray-700 pl-6">
            <span className="text-gray-500 text-[10px] uppercase font-bold">
              Total Net Worth:
            </span>
            <span
              className={`font-mono font-bold ${
                profitLoss >= 0 ? "text-green-400" : "text-red-400"
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
          <span className="text-gray-400 text-xs font-medium italic">
            Compte de {user?.name}
          </span>
          <button
            onClick={logout}
            className="p-2 text-gray-500 hover:text-red-400 transition-colors"
          >
            <LogOut size={18} />
          </button>
        </div>
      </header>

      {/* CONTENU PRINCIPAL */}
      <main className="flex-1 p-6 grid grid-cols-1 md:grid-cols-12 gap-6 max-w-[1600px] mx-auto w-full">
        {/* COLONNE GAUCHE : GRAPHIQUE ET HISTORIQUE */}
        <section className="col-span-12 md:col-span-8 flex flex-col gap-6">
          <div className="bg-crypto-card rounded-lg p-4 border border-gray-700 h-[500px] relative shadow-xl">
            <div className="absolute top-4 left-4 z-10 bg-black/60 p-3 rounded border border-gray-800 backdrop-blur-md">
              <h2 className="text-xl font-bold text-gray-100 leading-none">
                BTC/USD
              </h2>
              <p className="text-[10px] text-gray-500 mt-1 uppercase tracking-widest font-bold">
                Marché en direct
              </p>
            </div>
            <CandleChart data={history} stream={currentCandle} />
          </div>
          <TransactionHistory />
        </section>

        {/* COLONNE DROITE : TRADING ET WALLET */}
        <section className="col-span-12 md:col-span-4 flex flex-col gap-6">
          <TradePanel currentPrice={currentPrice} />

          <div className="bg-crypto-card rounded-lg p-6 border border-gray-700 shadow-lg">
            <h3 className="text-lg font-bold mb-6 flex items-center gap-2 text-gray-200">
              <Wallet size={20} className="text-blue-400" /> État du
              Portefeuille
            </h3>
            <div className="space-y-6">
              <div className="flex justify-between items-end border-b border-gray-800 pb-4">
                <span className="text-xs text-gray-500 uppercase font-bold">
                  Cash Disponible
                </span>
                <span className="text-xl font-mono text-green-400 font-bold">
                  $
                  {balance.toLocaleString(undefined, {
                    minimumFractionDigits: 2,
                  })}
                </span>
              </div>
              <div className="flex justify-between items-end border-b border-gray-800 pb-4">
                <span className="text-xs text-gray-500 uppercase font-bold">
                  Quantité BTC
                </span>
                <span className="text-xl font-mono text-blue-400 font-bold">
                  {btcHolding.toFixed(8)}
                </span>
              </div>
              <div className="bg-black/30 p-4 rounded-lg border border-gray-800">
                <div className="text-[10px] text-gray-500 uppercase mb-1">
                  Performance Globale
                </div>
                <div
                  className={`text-lg font-mono font-bold ${
                    profitLoss >= 0 ? "text-green-500" : "text-red-500"
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
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen bg-crypto-dark flex items-center justify-center text-white font-mono uppercase tracking-widest">
        Initialisation du terminal...
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

      {/* Page Privée (Chaque utilisateur a sa propre vue ici) */}
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
