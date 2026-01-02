import React, { useEffect, useRef } from 'react';
import { createChart, ColorType, CandlestickSeries } from 'lightweight-charts';

export const CandleChart = ({ data, stream }) => {
    const chartContainerRef = useRef();
    const seriesRef = useRef();

    useEffect(() => {
        const chart = createChart(chartContainerRef.current, {
            layout: {
                background: { type: ColorType.Solid, color: '#1a1a1a' },
                textColor: '#d1d5db',
                attributionLogo: false,
            },
            grid: {
                vertLines: { color: '#374151' },
                horzLines: { color: '#374151' },
            },
            width: chartContainerRef.current.clientWidth,
            height: 450,
            timeScale: {
                timeVisible: true,
                secondsVisible: true,
            },
        });

        const newSeries = chart.addSeries(CandlestickSeries, { 
            upColor: '#26a69a',
            downColor: '#ef5350',
            borderVisible: false,
            wickUpColor: '#26a69a',
            wickDownColor: '#ef5350',
        });
        
        // Load History
        if (data && data.length > 0) {
            newSeries.setData(data);
            chart.timeScale().fitContent(); // <--- CRITICAL FIX: Auto-zoom
        }
        
        seriesRef.current = newSeries;

        const handleResize = () => {
            chart.applyOptions({ width: chartContainerRef.current.clientWidth });
        };
        window.addEventListener('resize', handleResize);

        return () => {
            window.removeEventListener('resize', handleResize);
            chart.remove();
        };
    }, [data]); 

    // Handle Real-Time Updates
    useEffect(() => {
        if (stream && seriesRef.current) {
            seriesRef.current.update(stream);
        }
    }, [stream]);

    return <div ref={chartContainerRef} className="w-full h-full" />;
};