package in.tum.de.ase.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public final class TicketReaderClient extends JFrame implements Runnable, ThreadFactory {

	private static final long serialVersionUID = 6441489157408381878L;

	public static void main(final String[] args) {
		new TicketReaderClient();
	}

	private final Executor executor = Executors.newSingleThreadExecutor(this);
	private WebcamPanel panel = null;
	private JTextArea textarea = null;

	private Webcam webcam = null;

	public TicketReaderClient() {
		super();

		this.setLayout(new FlowLayout());
		this.setTitle("Scan Ticket");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final Dimension size = WebcamResolution.QVGA.getSize();

		this.webcam = Webcam.getWebcams().get(0);
		this.webcam.setViewSize(size);

		this.panel = new WebcamPanel(this.webcam);
		this.panel.setPreferredSize(size);

		this.textarea = new JTextArea();
		this.textarea.setEditable(false);
		this.textarea.setPreferredSize(size);

		this.add(this.panel);
		this.add(this.textarea);

		this.pack();
		this.setVisible(true);

		this.executor.execute(this);
	}

	@Override
	public Thread newThread(final Runnable r) {
		final Thread t = new Thread(r, "example-runner");
		t.setDaemon(true);
		return t;
	}

	@Override
	public void run() {

		do {
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			Result result = null;
			BufferedImage image = null;

			if (this.webcam.isOpen()) {

				if ((image = this.webcam.getImage()) == null) {
					continue;
				}

				final LuminanceSource source = new BufferedImageLuminanceSource(image);
				final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				try {
					result = new MultiFormatReader().decode(bitmap);
				} catch (final NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}
			}

			if (result != null) {
				this.textarea.setText(result.getText());
			}

		} while (true);
	}
}
