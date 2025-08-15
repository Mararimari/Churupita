package CoisaDoChurupita;
import java.awt.Color;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Churupita extends AdvancedRobot {

    private boolean movingForward = true;
    private double lastEnemyVelocity = 0;

    public void run() {
	
		setBodyColor(Color.PINK);
        setGunColor(Color.PINK);
        setRadarColor(Color.PINK);
        setBulletColor(Color.PINK);
        setScanColor(Color.PINK);

        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        while (true) {
            // Mantém radar girando até achar alvo
            turnRadarRight(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // ===== RADAR LOCK PRECISO =====
        double radarTurn = getHeading() + e.getBearing() - getRadarHeading();
        // Multiplica por 2 para garantir que o radar passe um pouco do alvo (overshoot)
        setTurnRadarRight(normalRelativeAngleDegrees(radarTurn) * 2);

        // ===== Escolha da mira =====
        double bulletPower = Math.min(3, Math.max(1, 400 / e.getDistance()));
        double targetAngle;

        if (Math.abs(e.getVelocity()) < 0.5) {
            targetAngle = getHeading() + e.getBearing(); // Head-On
        } else {
            double bulletSpeed = 20 - 3 * bulletPower;
            double time = e.getDistance() / bulletSpeed;
            double futureX = getX() + e.getVelocity() * time * Math.sin(Math.toRadians(getHeading() + e.getBearing()));
            double futureY = getY() + e.getVelocity() * time * Math.cos(Math.toRadians(getHeading() + e.getBearing()));
            targetAngle = Math.toDegrees(Math.atan2(futureX - getX(), futureY - getY()));
        }

        setTurnGunRight(normalRelativeAngleDegrees(targetAngle - getGunHeading()));

        if (getGunHeat() == 0 && getEnergy() > 1) {
            setFire(bulletPower);
        }

        // ===== Movimento evasivo com mudança de direção =====
        if (e.getVelocity() != lastEnemyVelocity) {
            reverseDirection();
        }
        lastEnemyVelocity = e.getVelocity();

        if (getTime() % 15 == 0) {
            setTurnRight(e.getBearing() + 90 - 30 + (Math.random() * 60 - 30));
            if (movingForward) {
                setAhead(120);
            } else {
                setBack(120);
            }
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        setTurnRight(90 - e.getBearing());
        reverseDirection();
    }

    public void onHitWall(HitWallEvent e) {
        reverseDirection();
    }

    private void reverseDirection() {
        if (movingForward) {
            setBack(150);
            movingForward = false;
        } else {
            setAhead(150);
            movingForward = true;
        }
    }
}
