package com.sebastianbinder.testblock;

import org.bukkit.util.Vector;

public enum Direction {
  NORTH(180), EAST(270), SOUTH(0), WEST(90);

  Integer rotation;

  Direction(Integer rotation) {
    this.rotation = rotation;
  }

  private static Direction getDirectionFromRotation(double rotation) {
    double direction = rotation / 90;
    if (direction <= 0.5 || direction > 3.5) {
      return SOUTH;
    } else if (direction > 0.5 && direction <= 1.5) {
      return WEST;
    } else if (direction > 1.5 && direction <= 2.5) {
      return NORTH;
    } else if (direction > 2.5 && direction <= 3.5) {
      return EAST;
    }
    return null;
  }

  public static Integer getOffset(Direction direction1, Direction direction2) {
   Integer offset = direction1.rotation - direction2.rotation;
    if (offset < 0) {
      offset += 360;
    }
    return offset;
  }

  public static double getLookAtYaw(Vector motion) {
    double dx = motion.getX();
    double dz = motion.getZ();
    double yaw = 0;
    // Set yaw
    if (dx != 0) {
      // Set yaw start value based on dx
      if (dx < 0) {
        yaw = 1.5 * Math.PI;
      } else {
        yaw = 0.5 * Math.PI;
      }
      yaw -= Math.atan(dz / dx);
    } else if (dz < 0) {
      yaw = Math.PI;
    }

    yaw = Math.toDegrees(-yaw);
    if (yaw < 0) {
      yaw += 360.0;
    }
    return yaw;
  }

  public static Direction getDirectionFromVector(Vector vector) {
    return getDirectionFromRotation(getLookAtYaw(vector));
  }
}
