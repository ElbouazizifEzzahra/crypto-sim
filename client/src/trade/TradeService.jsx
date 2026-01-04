// This simulates sending the trade to a backend
export const executeTrade = async (tradeData) => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      // Basic validation
      if (tradeData.amount <= 0) {
        reject(new Error("Amount must be greater than 0"));
        return;
      }

      console.log("Executing Trade:", tradeData);

      // In a real app, the backend would verify balance here
      resolve({
        success: true,
        orderId: Math.random().toString(36).substr(2, 9),
        ...tradeData,
      });
    }, 600);
  });
};
