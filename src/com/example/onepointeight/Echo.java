package com.example.onepointeight;

import java.io.IOException;

public class Echo {
    public static void main(String[] args) throws IOException {
        Reactor.run(reactor ->
                reactor.listen(3000, client ->
                        reactor.read(client, data -> {
                            data.flip();
                            reactor.write(client, data);
                        })
                )
        );
    }
}
