package CoisaDoChurupita;
import robocode.*;
import java.awt.Color;

public class Churupita extends AdvancedRobot {
    private double enemyDistance = 0;
    private String targetName = null;

    public void run() {
        
        setBodyColor(Color.PINK);
        setGunColor(Color.PINK);
        setRadarColor(Color.PINK);
        setBulletColor(Color.PINK);
        setScanColor(Color.PINK);

        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        while (true) {
            turnRadarRight(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        enemyDistance = e.getDistance();
        targetName = e.getName();

        
        double radarTurn = getHeading() - getRadarHeading() + e.getBearing();
        setTurnRadarRight(normaliseBearing(radarTurn));

        
        double firePower;
        if (enemyDistance > 400) { 
            firePower = 1.5;
            setTurnRight(e.getBearing()); 
            setAhead(50);
        } else { 
            firePower = 3;
            setTurnRight(e.getBearing() + 90); 
            setAhead(150);
        }

        
        double turnGunAmt = normaliseBearing(e.getBearing() + getHeading() - getGunHeading());
        setTurnGunRight(turnGunAmt);

        
        if (getGunHeat() == 0 && getEnergy() > 0.5) {
            fire(firePower);
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        targetName = e.getName();
        double turnGunAmt = normaliseBearing(e.getBearing() + getHeading() - getGunHeading());
        setTurnGunRight(turnGunAmt);
        fire(3); 
        back(50);
    }

    
    private double normaliseBearing(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }
	
	public void onWin(WinEvent e) {
        for (int i = 0; i < 50; i++) {
            turnRight(30);
            turnLeft(30);
        }
    }
}
