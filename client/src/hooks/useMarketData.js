import { useMockTicker } from './useMockTicker';
import { useSocketTicker } from './useSocketTicker';

// TOGGLE THIS: true = Mock Mode, false = Real Backend
const USE_MOCK_DATA = true; 

export const useMarketData = () => {
    // 1. Get Mock Data
    const mockData = useMockTicker(95000, USE_MOCK_DATA);

    // 2. Get Real Data (Placeholder logic for now)
    const realDataRaw = useSocketTicker(!USE_MOCK_DATA);
    
    // Normalize real data to match the mock structure
    // (Real backend might only send the live candle, so history is empty for now)
    const realData = { 
        currentCandle: realDataRaw, 
        history: [] 
    }; 

    return USE_MOCK_DATA ? mockData : realData;
};