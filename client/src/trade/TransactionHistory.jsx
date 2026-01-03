import React from "react";
import { usePortfolio } from "../portfolio/PortfolioContext";
import { History } from "lucide-react";

const TransactionHistory = () => {
  const { transactions } = usePortfolio();

  return (
    <div className="bg-crypto-card rounded-lg border border-gray-700 shadow-xl overflow-hidden">
      <div className="p-4 border-b border-gray-800 flex items-center gap-2">
        <History size={18} className="text-blue-400" />
        <h3 className="font-bold text-gray-200">Recent Transactions</h3>
      </div>
      <div className="max-h-[300px] overflow-y-auto">
        <table className="w-full text-left text-xs">
          <thead className="bg-black/50 text-gray-400 sticky top-0">
            <tr>
              <th className="p-3 font-semibold uppercase tracking-wider">
                Date/Time
              </th>
              <th className="p-3 font-semibold uppercase tracking-wider">
                Type
              </th>
              <th className="p-3 font-semibold uppercase tracking-wider">
                Price
              </th>
              <th className="p-3 font-semibold uppercase tracking-wider">
                Quantity
              </th>
              <th className="p-3 font-semibold uppercase tracking-wider text-right">
                Total USD
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-800">
            {transactions.length === 0 ? (
              <tr>
                <td
                  colSpan="5"
                  className="p-8 text-center text-gray-500 italic"
                >
                  No trades executed yet.
                </td>
              </tr>
            ) : (
              transactions.map((tx, idx) => (
                <tr
                  key={idx}
                  className="hover:bg-white/5 transition-colors group"
                >
                  <td className="p-3 text-gray-500">
                    {new Date(tx.timestamp).toLocaleDateString()} <br />
                    <span className="text-[10px]">
                      {new Date(tx.timestamp).toLocaleTimeString()}
                    </span>
                  </td>
                  <td className="p-3">
                    <span
                      className={`px-2 py-0.5 rounded-full font-bold text-[10px] ${
                        tx.type === "BUY"
                          ? "bg-green-500/10 text-green-500"
                          : "bg-red-500/10 text-red-500"
                      }`}
                    >
                      {tx.type}
                    </span>
                  </td>
                  <td className="p-3 font-mono text-gray-300">
                    ${tx.price.toLocaleString()}
                  </td>
                  <td className="p-3 font-mono text-gray-300">
                    {tx.quantity.toFixed(6)} BTC
                  </td>
                  <td className="p-3 font-mono text-right font-bold text-gray-100">
                    $
                    {(tx.quantity * tx.price).toLocaleString(undefined, {
                      minimumFractionDigits: 2,
                    })}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default TransactionHistory;
