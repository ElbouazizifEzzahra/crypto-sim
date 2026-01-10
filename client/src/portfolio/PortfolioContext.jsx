import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  useCallback,
} from "react";
// 1. Correct Imports
import { AuthContext } from "../auth/authContext";
import { httpClient } from "../api/httpClient";
import { CheckCircle, XCircle } from "lucide-react";

const PortfolioContext = createContext(null);

export const PortfolioProvider = ({ children }) => {
  // 2. Use Standard Context (No useAuth)
  const { user } = useContext(AuthContext);

  // State
  const [balance, setBalance] = useState(0);
  const [portfolioItems, setPortfolioItems] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [isSyncing, setIsSyncing] = useState(false);
  const [notification, setNotification] = useState(null);

  // --- SOUND SYSTEM (UNCHANGED) ---
  const playSound = useCallback((type) => {
    const audio = new Audio(type === "success" ? "/success.mp3" : "/error.mp3");
    audio.volume = 0.4;
    audio.play().catch(() => {}); // Ignore browser autoplay blocks
  }, []);

  // --- NOTIFICATION SYSTEM (UNCHANGED) ---
  const notify = (message, type = "success") => {
    setNotification({ message, type });
    playSound(type);
    setTimeout(() => setNotification(null), 3500);
  };

  // --- FETCH REAL DATA ---
  const loadPortfolioData = useCallback(async () => {
    if (!user) return;

    try {
      // A. Fetch Wallet/Portfolio
      const data = await httpClient.get("/portfolio"); 
      // Assuming Backend returns: { balance: 10000, assets: [...] }
      setBalance(data.balance || 0);
      setPortfolioItems(data.assets || []);

      // B. Fetch Transaction History (Optional - creates separate endpoint call)
      // const history = await httpClient.get("/trade/history");
      // setTransactions(history);
      
    } catch (error) {
      console.error("Failed to load portfolio:", error);
    }
  }, [user]);

  // Load on login
  useEffect(() => {
    loadPortfolioData();
  }, [loadPortfolioData]);

  // --- EXECUTE REAL TRADE ---
  const executeTrade = async (type, symbol, usdAmount, currentPrice) => {
    if (isSyncing) return;
    setIsSyncing(true);

    try {
      // 1. Calculate Quantity (Backend expects amount of BTC, not USD)
      // Note: Ensure your backend handles 'quantity' precision correctly
      const quantity = usdAmount / currentPrice;

      // 2. Send Request to Spring Boot
      // Endpoint: /api/trade/execute (Matches TradeController)
      await httpClient.post("/trade/execute", {
        symbol: symbol,
        quantity: quantity,
        type: type, // "BUY" or "SELL"
        price: currentPrice // Send price for record-keeping or limit orders
      });

      // 3. Success!
      if (type === "BUY") {
        notify(`Bought ${quantity.toFixed(6)} ${symbol}`, "success");
      } else {
        notify(`Sold ${quantity.toFixed(6)} ${symbol}`, "success");
      }

      // 4. Refresh Data (Update Balance & Portfolio instantly)
      await loadPortfolioData();

    } catch (error) {
      // 5. Handle Error (e.g., "Insufficient Funds")
      console.error("Trade Failed:", error);
      // Extract error message from Backend response if possible
      const msg = error.message || "Trade failed. Please try again.";
      notify(msg, "error");
    } finally {
      setIsSyncing(false);
    }
  };

  return (
    <PortfolioContext.Provider
      value={{
        balance,
        portfolioItems,
        transactions,
        executeTrade,
        isSyncing,
        refreshPortfolio: loadPortfolioData
      }}
    >
      {children}

      {/* --- NOTIFICATION UI (UNCHANGED) --- */}
      {notification && (
        <div className="fixed top-8 right-8 z-[9999] pointer-events-none">
          <div
            className={`flex items-center gap-4 px-6 py-4 rounded-xl shadow-[0_20px_50px_rgba(0,0,0,0.5)] border transition-all duration-500 animate-in fade-in slide-in-from-right-10 ${
              notification.type === "success"
                ? "bg-emerald-500/20 border-emerald-500/50 text-emerald-400"
                : "bg-rose-500/20 border-rose-500/50 text-rose-400"
            } backdrop-blur-xl`}
          >
            {notification.type === "success" ? (
              <CheckCircle className="animate-bounce" size={24} />
            ) : (
              <XCircle size={24} />
            )}
            <div>
              <p className="text-[10px] uppercase tracking-widest font-black opacity-60">
                System Message
              </p>
              <p className="font-bold text-sm leading-tight">
                {notification.message}
              </p>
            </div>
          </div>
        </div>
      )}
    </PortfolioContext.Provider>
  );
};

// Helper Hook
export const usePortfolio = () => useContext(PortfolioContext);