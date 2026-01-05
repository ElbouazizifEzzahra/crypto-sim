import React, { useState } from "react";
import { usePortfolio } from "../portfolio/PortfolioContext";
import { ArrowUpCircle, ArrowDownCircle, Loader2 } from "lucide-react";

const TradePanel = ({ currentPrice }) => {
  const [amount, setAmount] = useState("");
  const { executeTrade, isSyncing } = usePortfolio();

  const handleAction = async (type) => {
    const usd = parseFloat(amount);
    if (!usd || usd <= 0) return; // Silent return, or add a notify call in context

    await executeTrade(type, "BTC", usd, currentPrice);
    setAmount(""); // Reset input on completion
  };

  return (
    <div className="bg-crypto-card rounded-lg p-5 border border-gray-700 shadow-lg">
      <h3 className="text-lg font-bold mb-4 text-gray-200">Order Entry</h3>

      <div className="space-y-4">
        <div className="relative">
          <span className="absolute left-3 top-3.5 text-gray-500 font-mono">
            $
          </span>
          <input
            type="number"
            placeholder="0.00"
            value={amount}
            disabled={isSyncing}
            onChange={(e) => setAmount(e.target.value)}
            className="w-full p-3 pl-7 bg-black border border-gray-600 rounded text-white focus:outline-none focus:border-blue-500 transition font-mono disabled:opacity-50"
          />
        </div>

        <div className="bg-black/40 p-3 rounded border border-gray-800/50">
          <div className="flex justify-between text-[11px] font-mono">
            <span className="text-gray-500 uppercase">Est. Quantity</span>
            <span className="text-blue-400 font-bold">
              {amount && currentPrice
                ? (amount / currentPrice).toFixed(8)
                : "0.00000000"}{" "}
              BTC
            </span>
          </div>
        </div>

        <div className="grid grid-cols-2 gap-3">
          <button
            onClick={() => handleAction("BUY")}
            disabled={isSyncing || !amount}
            className="flex items-center justify-center gap-2 bg-trade-green hover:brightness-110 active:scale-95 disabled:opacity-50 text-white font-bold py-3 rounded transition-all"
          >
            {isSyncing ? (
              <Loader2 size={18} className="animate-spin" />
            ) : (
              <>
                <ArrowUpCircle size={18} /> BUY
              </>
            )}
          </button>

          <button
            onClick={() => handleAction("SELL")}
            disabled={isSyncing || !amount}
            className="flex items-center justify-center gap-2 bg-trade-red hover:brightness-110 active:scale-95 disabled:opacity-50 text-white font-bold py-3 rounded transition-all"
          >
            {isSyncing ? (
              <Loader2 size={18} className="animate-spin" />
            ) : (
              <>
                <ArrowDownCircle size={18} /> SELL
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  );
};

export default TradePanel;
