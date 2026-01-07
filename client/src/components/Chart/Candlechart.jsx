import React, { useEffect, useRef } from 'react';
import { createChart, ColorType, AreaSeries } from 'lightweight-charts'; // <--- CHANGED IMPORT

export const CandleChart = ({ data, stream }) => {
    const chartContainerRef = useRef();
    const seriesRef = useRef();
    const chartRef = useRef();

    useEffect(() => {
        const chart = createChart(chartContainerRef.current, {
            layout: {
                background: { type: ColorType.Solid, color: '#1a1a1a' },
                textColor: '#d1d5db',
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

        // <--- SWITCHED TO AREA SERIES (Best for single-price streams)
        const newSeries = chart.addSeries(AreaSeries, {
            lineColor: '#26a69a', 
            topColor: 'rgba(38, 166, 154, 0.4)',
            bottomColor: 'rgba(38, 166, 154, 0.0)',
            lineWidth: 2,
        });
        
        // Load Initial History
        if (data && data.length > 0) {
            newSeries.setData(data);
            chart.timeScale().fitContent();
        }
        
        seriesRef.current = newSeries;
        chartRef.current = chart;

        const handleResize = () => {
            chart.applyOptions({ width: chartContainerRef.current.clientWidth });
        };
        window.addEventListener('resize', handleResize);

        return () => {
            window.removeEventListener('resize', handleResize);
            chart.remove();
        };
    }, []); // Only run once on mount

    // Handle Real-Time Updates
    useEffect(() => {
        if (stream && seriesRef.current) {
            seriesRef.current.update(stream);
        }
    }, [stream]);

    return <div ref={chartContainerRef} className="w-full h-full" />;
};