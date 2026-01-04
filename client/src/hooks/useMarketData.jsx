import React, { createContext, useContext } from "react";
import { useMockTicker } from "./useMockTicker";
import { useSocketTicker } from "./useSocketTicker";

const MarketDataContext = createContext();
const USE_MOCK_DATA = true;

export const MarketDataProvider = ({ children }) => {
  const mockData = useMockTicker(95000, USE_MOCK_DATA);
  const realDataRaw = useSocketTicker(!USE_MOCK_DATA);

  const realData = {
    currentCandle: realDataRaw,
    history: [],
  };

  const value = USE_MOCK_DATA ? mockData : realData;

  return (
    <MarketDataContext.Provider value={value}>
      {children}
    </MarketDataContext.Provider>
  );
};

export const useMarketData = () => {
  const context = useContext(MarketDataContext);
  if (!context) {
    throw new Error("useMarketData must be used within a MarketDataProvider");
  }
  return context;
};
