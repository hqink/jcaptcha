/*
 * JCaptcha, the open source java framework for captcha definition and integration
 * Copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.image;

import com.octo.captcha.Captcha;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p>String question about a BufferedImage challenge. Abstract.</p>
 *
 * @author <a href="mailto:mag@jcaptcha.net">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public abstract class ImageCaptcha implements Captcha {

    private Boolean hasChallengeBeenCalled = Boolean.FALSE;

    protected String question;

    protected transient BufferedImage challenge;

    protected ImageCaptcha(String question, BufferedImage challenge) {
        this.challenge = challenge;
        this.question = question;
    }

    /**
     * Accessor captcha question
     *
     * @return the question
     */
    public final String getQuestion() {
        return question;
    }

    /**
     * @return the challenge
     */
    public final Object getChallenge() {
        return getImageChallenge();
    }

    /**
     * @return the image challenge
     */
    public final BufferedImage getImageChallenge() {
        hasChallengeBeenCalled = Boolean.TRUE;
        return challenge;
    }

    ;


    /**
     * Dispose the challenge, once this method is call the getChallenge method will return null.<br> It has been added
     * for technical reasons : a captcha is always used in a two step fashion<br> First submit the challenge, and then
     * wait until the response arrives.<br> It had been asked to have a method to dispose the challenge that is no
     * longer used after being dipslayed. So here it is!
     */
    public final void disposeChallenge() {
        this.challenge = null;
    }

    /**
     * This method should return true if the getChalenge method has been called (has been added in order to properly
     * manage the captcha state.
     *
     * @return true if getChallenge has been called false otherwise.
     */
    public Boolean hasGetChalengeBeenCalled() {
        return hasChallengeBeenCalled;
    }

    //use jpeg encoding
    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {

        if (this.challenge != null) {
            out.defaultWriteObject();
            // a jpeg encoder
            JPEGImageEncoder jpegEncoder =
                    JPEGCodec.createJPEGEncoder(out);
            jpegEncoder.encode(this.challenge);
        }
    }

    ;

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
        this.challenge = decoder.decodeAsBufferedImage();
    }

    ;


}
