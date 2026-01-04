import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import { AuthProvider } from "./auth/authContext";
import { PortfolioProvider } from "./portfolio/PortfolioContext";
import { MarketDataProvider } from "./hooks/useMarketData"; // <--- AJOUTÉ
import "./index.css";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <MarketDataProvider>
          {" "}
          {/* AJOUTÉ */}
          <PortfolioProvider>
            <App />
          </PortfolioProvider>
        </MarketDataProvider>
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
);
