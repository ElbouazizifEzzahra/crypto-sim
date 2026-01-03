import React from 'react';
import { TrendingUp, Wallet } from 'lucide-react';
import { CandleChart } from './components/Chart/Candlechart.jsx';
import { useMarketData } from './hooks/useMarketData';

function App() {
  // Extract data correctly
  const { currentCandle, history } = useMarketData(); 

  return (
    <div className="min-h-screen bg-crypto-dark text-white flex flex-col">
      
      {/* HEADER */}
      <header className="h-12 bg-black border-b border-gray-800 flex items-center px-4">
        <div className="flex items-center gap-2 text-green-400 font-mono text-sm animate-pulse">
          <TrendingUp size={16} />
          {/* Safe check for price */}
          <span>BTC: ${currentCandle?.close?.toFixed(2) || '---'}</span>
        </div>
      </header>

      {/* MAIN LAYOUT */}
      <main className="flex-1 p-6 grid grid-cols-1 md:grid-cols-12 gap-6">
        
        {/* CHART SECTION */}
        <section className="col-span-12 md:col-span-8 flex flex-col gap-4">
          <div className="bg-crypto-card rounded-lg p-4 border border-gray-700 h-[500px] relative shadow-xl overflow-hidden">
             <div className="absolute top-4 left-4 z-10">
                <h2 className="text-xl font-bold text-gray-200">BTC/USD</h2>
                <p className={`text-sm font-mono ${!currentCandle ? 'text-gray-500' : 'text-green-400'}`}>
                    {currentCandle ? `$${currentCandle.close.toFixed(2)}` : 'Connecting...'}
                </p>
             </div>

             {/* Pass BOTH history and stream */}
             <CandleChart data={history} stream={currentCandle} /> 
          </div>
        </section>

        {/* SIDEBAR (Trade & Assets) */}
        <section className="col-span-12 md:col-span-4 flex flex-col gap-6">
          
          <div className="bg-crypto-card rounded-lg p-5 border border-gray-700 shadow-lg">
            <h3 className="text-lg font-bold mb-4 flex items-center gap-2">
              Make a Trade
            </h3>
            <div className="space-y-3">
              <input 
                type="number" 
                placeholder="Amount (USD)" 
                className="w-full p-3 bg-black border border-gray-600 rounded text-white focus:outline-none focus:border-blue-500 transition"
              />
              <div className="grid grid-cols-2 gap-3">
                <button className="bg-trade-green hover:brightness-110 text-white font-bold py-3 rounded transition">
                  BUY
                </button>
                <button className="bg-trade-red hover:brightness-110 text-white font-bold py-3 rounded transition">
                  SELL
                </button>
              </div>
            </div>
          </div>

          <div className="bg-crypto-card rounded-lg p-5 border border-gray-700 shadow-lg flex-1">
            <h3 className="text-lg font-bold mb-4 flex items-center gap-2">
              <Wallet size={20} className="text-blue-400"/> Your Assets
            </h3>
            <div className="space-y-4 text-sm">
              <div className="flex justify-between border-b border-gray-600 pb-2">
                <span className="text-gray-400">Cash Balance</span>
                <span className="font-mono text-xl">$10,000.00</span>
              </div>
              <div className="flex justify-between border-b border-gray-600 pb-2">
                <span className="text-gray-400">BTC Owned</span>
                <span className="font-mono text-xl">0.00</span>
              </div>
            </div>
          </div>

        </section>
      </main>
    </div>
  );
}

export default App;
