import React, { createContext, useContext } from "react";
import { useMockTicker } from "./useMockTicker";
import { useSocketTicker } from "./useSocketTicker";

const MarketDataContext = createContext();

// Use ENV variable for switching (Defaults to Mock if not set)
// In .env file: VITE_USE_MOCK=false
const USE_MOCK_DATA = import.meta.env.VITE_USE_MOCK === 'true';

export const MarketDataProvider = ({ children }) => {
  // 1. Always call hooks (Rules of Hooks), but control execution with the boolean
  const mockData = useMockTicker(95000, USE_MOCK_DATA);
  const realData = useSocketTicker(!USE_MOCK_DATA);

  // 2. The Facade Logic: Pick one source
  // Since we normalized useSocketTicker, we don't need manual object construction here
  const value = USE_MOCK_DATA ? mockData : realData;

  return (
    <MarketDataContext.Provider value={value}>
      {children}
    </MarketDataContext.Provider>
  );
};

export const useMarketData = () => {
  return useContext(MarketDataContext);
};