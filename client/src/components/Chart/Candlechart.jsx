import React, { useEffect, useRef } from "react";
import { createChart, ColorType, CandlestickSeries } from "lightweight-charts";

export const CandleChart = ({ data, stream }) => {
  const chartContainerRef = useRef();
  const seriesRef = useRef();
  const chartRef = useRef(); // Keep reference to chart to use methods later

  useEffect(() => {
    const chart = createChart(chartContainerRef.current, {
      attributionLogo: false,
      layout: {
        background: { type: ColorType.Solid, color: "#131722" },
        textColor: "#d1d4dc",
      },
      grid: {
        vertLines: { color: "rgba(42, 46, 57, 0.5)" },
        horzLines: { color: "rgba(42, 46, 57, 0.5)" },
      },
      width: chartContainerRef.current.clientWidth,
      height: 550,
      timeScale: {
        timeVisible: true,
        secondsVisible: false,
        borderColor: "#2B2B43",
      },
      rightPriceScale: {
        borderColor: "#2B2B43",
      },
    });

    const newSeries = chart.addSeries(CandlestickSeries, {
      upColor: "#089981",
      downColor: "#F23645",
      borderVisible: false,
      wickUpColor: "#089981",
      wickDownColor: "#F23645",
    });

    seriesRef.current = newSeries;
    chartRef.current = chart; // Save chart instance

    const handleResize = () => {
      chart.applyOptions({ width: chartContainerRef.current.clientWidth });
    };
    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
      chart.remove();
    };
  }, []);

  // ⚡️ EFFECT: Load History (Fake or Real)
  useEffect(() => {
    if (data && data.length > 0 && seriesRef.current) {
      seriesRef.current.setData(data);
      chartRef.current.timeScale().fitContent(); // Auto-zoom to fit history
    }
  }, [data]); // Run whenever 'data' changes

  // ⚡️ EFFECT: Live Updates
  useEffect(() => {
    if (stream && seriesRef.current) {
      seriesRef.current.update(stream);
    }
  }, [stream]);

  return <div ref={chartContainerRef} className="w-full h-full" />;
};
