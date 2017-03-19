package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by James on 2017-03-18.
 */

class Tracker {
    //TODO: Add multithreading

    static VuforiaTrackables mMarkers;

    static void init() {
        mMarkers = VuforiaWrapper.mInstance.loadTrackablesFromAsset("FTC_2016-17");

        mMarkers.get(0).setName("Wheels");
        mMarkers.get(1).setName("Tools");
        mMarkers.get(2).setName("Legos");
        mMarkers.get(3).setName("Gears");

        mMarkers.activate();
    }

    static VectorF getPose() {
        for(VuforiaTrackable m : mMarkers) {
            VuforiaTrackableDefaultListener l = (VuforiaTrackableDefaultListener) m.getListener();

            if(l.getPose() != null) {
                VectorF translation = l.getPose().getTranslation();
                float angle = getEulerRotation(l.getPose()).get(1);

                //Remapping pose to adjust for landscape orientation
                return new VectorF(-translation.get(1), translation.get(0), translation.get(2), angle);
            }
        }

        return null;
    }

    private static VectorF getEulerRotation(OpenGLMatrix pose) {
        double heading, pitch, roll;

        // Assuming the angles are in radians.
        if (pose.get(1, 0) > 0.998) { // singularity at north pole
            heading = Math.atan2(pose.get(0, 2), pose.get(2, 2));
            pitch = Math.PI/2;
            roll = 0;
        }
        else if (pose.get(1, 0) < -0.998) { // singularity at south pole
            heading = Math.atan2(pose.get(0, 2), pose.get(2, 2));
            pitch = -Math.PI/2;
            roll = 0;
        }
        else {
            heading = Math.atan2(pose.get(2, 0), pose.get(0, 0));
            pitch = Math.atan2(-pose.get(1, 2), pose.get(1, 1));
            roll = Math.asin(pose.get(1, 0));
        }

        return new VectorF((float) Math.toDegrees(roll), (float) Math.toDegrees(heading), (float) Math.toDegrees(pitch));
    }
}
