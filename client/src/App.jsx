import React, { useState } from "react";
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

// --- THE INDIVIDUAL DASHBOARD PAGE ---
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
      <header className="h-14 bg-black border-b border-gray-800 flex items-center justify-between px-6">
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
          <span className="text-gray-400 text-xs font-medium">
            Hello, {user?.name}
          </span>
          <button
            onClick={logout}
            className="text-gray-500 hover:text-red-400 transition-colors"
          >
            <LogOut size={18} />
          </button>
        </div>
      </header>

      <main className="flex-1 p-6 grid grid-cols-1 md:grid-cols-12 gap-6 max-w-[1600px] mx-auto w-full">
        <section className="col-span-12 md:col-span-8 flex flex-col gap-6">
          <div className="bg-crypto-card rounded-lg p-4 border border-gray-700 h-[500px] relative shadow-xl">
            <CandleChart data={history} stream={currentCandle} />
          </div>
          <TransactionHistory />
        </section>

        <section className="col-span-12 md:col-span-4 flex flex-col gap-6">
          <TradePanel currentPrice={currentPrice} />
          <div className="bg-crypto-card rounded-lg p-6 border border-gray-700 shadow-lg">
            <h3 className="text-lg font-bold mb-6 flex items-center gap-2 text-gray-200">
              <Wallet size={20} className="text-blue-400" /> Wallet Overview
            </h3>
            <div className="space-y-6">
              <div className="flex justify-between items-end border-b border-gray-800 pb-4">
                <span className="text-xs text-gray-500 uppercase font-bold">
                  Available Cash
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
                  BTC Balance
                </span>
                <span className="text-xl font-mono text-blue-400 font-bold">
                  {btcHolding.toFixed(8)}
                </span>
              </div>
              <div className="bg-black/30 p-4 rounded-lg border border-gray-800">
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

// --- MAIN APP ROUTER ---
function App() {
  const { isAuthenticated, loading } = useAuth();

  if (loading)
    return (
      <div className="min-h-screen bg-crypto-dark flex items-center justify-center text-white">
        Loading Session...
      </div>
    );

  return (
    <Routes>
      {/* If logged in, / sends you to dashboard. If not, sends you to login. */}
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

      {/* Individual Pages */}
      <Route
        path="/login"
        element={!isAuthenticated ? <Login /> : <Navigate to="/dashboard" />}
      />
      <Route
        path="/register"
        element={!isAuthenticated ? <Register /> : <Navigate to="/dashboard" />}
      />

      {/* Protected Dashboard Page */}
      <Route
        path="/dashboard"
        element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" />}
      />

      {/* 404 Redirect */}
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
}

export default App;
