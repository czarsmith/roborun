package hcs.roborun.ui.widgets;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.DOMImplementation;

public class SvgImage extends ImageTranscoder {
  private BufferedImage image = null;

  public SvgImage(String uri, float width, float height) {
    TranscodingHints hints = new TranscodingHints();
    hints.put(ImageTranscoder.KEY_WIDTH, width);
    hints.put(ImageTranscoder.KEY_HEIGHT, height);
    DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
    hints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION, impl);
    hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI,SVGConstants.SVG_NAMESPACE_URI);
    hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI,SVGConstants.SVG_NAMESPACE_URI);
    hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT, SVGConstants.SVG_SVG_TAG);
    hints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, false);
    setTranscodingHints(hints);

    try (InputStream is = getClass().getResourceAsStream(uri)) {
      transcode(new TranscoderInput(is), null);
    } catch (Exception e) { e.printStackTrace(); }
  }

  public BufferedImage createImage(int w, int h) {
    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    return image;
  }

  public void writeImage(BufferedImage img, TranscoderOutput out) {
  }

  public BufferedImage getImage() {
    return image;
  }
}
