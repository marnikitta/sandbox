import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public final class NioTest {
  public static void main(final String... args) throws IOException {
    new NioTest().run();
  }

  public void run() throws IOException {
    final AsynchronousSocketChannel async = AsynchronousSocketChannel.open();
    async.bind(new InetSocketAddress(InetAddress.getLocalHost(), 12345));
    final ByteBuffer buff = ByteBuffer.allocate(1024);
    async.read(buff, null, new Handler());
  }

  private final class Handler implements CompletionHandler<Integer, Void> {

    @Override
    public void completed(final Integer result, final Void attachment) {
      System.out.println(result);
    }

    @Override
    public void failed(final Throwable exc, final Void attachment) {
      System.out.println(exc);
    }
  }
}
