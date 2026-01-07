import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import "./index.css";

// 1. FIXED: Import AuthProvider from the correct file (AuthProvider.jsx)
import { AuthProvider } from "./auth/AuthProvider"; 

// 2. FIXED: Import the other Providers
import { PortfolioProvider } from "./portfolio/PortfolioContext";
import { MarketDataProvider } from "./hooks/useMarketData"; // <--- RESTORE THIS!

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      {/* 1. Auth wraps everything (User Session) */}
      <AuthProvider>
          
          {/* 2. Market Data (Prices/Charts) */}
          <MarketDataProvider>
            
            {/* 3. Portfolio (Balance/Transactions) */}
            <PortfolioProvider>
                <App />
            </PortfolioProvider>

          </MarketDataProvider>
          
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
);