/*
 * Copyright 2009-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package griffon.plugins.slideware;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * A container that draws an image.
 *
 * @author Andres Almiray
 */
public class ImagePanel extends BackgroundPanel {
    private static final Color TRANSPARENT = new Color(255, 0, 0, 255);
    private float scale = 1.0f;
    private Image image;

    public ImagePanel() {
        setBackground(TRANSPARENT);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setImagePath(String imagePath) {
        try {
            image = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource(imagePath));
        } catch (IOException e) {
            // ignore ??
        }
    }

    protected void paintImage(Graphics2D g, Rectangle bounds) {
        super.paintImage(g, bounds);
        if (image != null) {
            GraphicsUtil.scaleAndDrawImage(g, scale, image);
        }
    }
}
