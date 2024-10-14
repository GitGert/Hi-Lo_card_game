import React, { useState, useEffect } from 'react';
import './StopwatchAndHealthBar.css';

function StopwatchAndHealthBar() {
  // Stopwatch state
  const [seconds, setSeconds] = useState(0);
  const [minutes, setMinutes] = useState(0);

  // Health bar state
  const [health, setHealth] = useState(100);

  // Update stopwatch every second
  useEffect(() => {
    const interval = setInterval(() => {
      setSeconds(prevSeconds => prevSeconds + 1);
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  // Function to update health
  const updateHealth = (amount) => {
    setHealth(prevHealth => Math.max(0, Math.min(100, prevHealth + amount)));
  };

  return (
    <div className="stopwatch-and-health-bar">
      <div className="stopwatch">
        <span>{minutes.toString().padStart(2, '0')}:{seconds.toString().padStart(2, '0')}</span>
      </div>
      <div className="health-bar">
        <div className="heart" style={{ width: `${health}%` }} />
      </div>
    </div>
  );
}

export default StopwatchAndHealthBar;