package CoisaDoChurupita;
import robocode.*;
import java.awt.Color;

public class Churupita extends AdvancedRobot {
    private double enemyDistance = 0;
    private String targetName = null;

    public void run() {
        // Cores do Churupita
        setBodyColor(Color.PINK);
        setGunColor(Color.PINK);
        setRadarColor(Color.PINK);
        setBulletColor(Color.PINK);
        setScanColor(Color.PINK);

        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        while (true) {
            // Radar girando constantemente
            turnRadarRight(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        enemyDistance = e.getDistance();
        targetName = e.getName();

        // Travar radar no inimigo
        double radarTurn = getHeading() - getRadarHeading() + e.getBearing();
        setTurnRadarRight(normaliseBearing(radarTurn));

        // Precisão de tiro
        double firePower;
        if (enemyDistance > 400) { // Longa distância
            firePower = 1.5;
            setTurnRight(e.getBearing()); // Menos movimento
            setAhead(50);
        } else { // Curta distância
            firePower = 3;
            setTurnRight(e.getBearing() + 90); // Movimento evasivo
            setAhead(150);
        }

        // Mira no inimigo
        double turnGunAmt = normaliseBearing(e.getBearing() + getHeading() - getGunHeading());
        setTurnGunRight(turnGunAmt);

        // Atirar sempre que possível
        if (getGunHeat() == 0 && getEnergy() > 0.5) {
            fire(firePower);
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        targetName = e.getName();
        double turnGunAmt = normaliseBearing(e.getBearing() + getHeading() - getGunHeading());
        setTurnGunRight(turnGunAmt);
        fire(3); // Tiro forte a curta distância
        back(50);
    }

    // Função para normalizar ângulo
    private double normaliseBearing(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }
}
