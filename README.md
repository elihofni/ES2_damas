# ES2_damas
##Jogo de damas.

##Classe Regras:

       regras.setOnBoardChangedListener(new Regras.BoardChangedListener() {
            @Override
            public void onPieceMoved(Posicao posicao, Posicao posicao1) {
            
            }

            @Override
            public void onGameFinished(int i, int i1) {
            
            }

            @Override
            public void onPieceRemoved(Posicao posicao) {
            
            }

            @Override
            public void virouDama(int i, int i1, int i2) {
            
            }
     });

Chamada sempre que uma peça tem um movimento bem sucedido.

`onPieceMoved(Posicao posInicial, Posicao posFinal);`

Chamada sempre depois que o jogo termina.

`onGameFinished(int causa, int timeVencedor);`

Chamada sempre que uma peça é removida do tabuleiro.

`onPieceRemoved(Posicao pos);`

Chamada sempre que uma peça vira dama.

`virouDama(Posicao pos, int time);`
