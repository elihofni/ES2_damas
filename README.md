            public void onPieceMoved(Posicao posInicial, Posicao posFinal) {
            
            }

            @Override
            public void onGameFinished(int causa, int timeVencedor) {
            
            }

            @Override
            public void onPieceRemoved(Posicao posicao) {
            
            }

            @Override
            public void virouDama(int i, int j, int time) {
            
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

## TabuleiroView
       tabuleiroView.setOnClickTabuleiro(new TabuleiroView.OnClickTabuleiro() {
            @Override
            public void onClickPeca(Pos pos) {
                
            }

            @Override
            public void onClickCasa(Pos posPeca, Pos posCasa) {
                
            }
           });
           
    
Chamada sempre que uma peça é selecionada.

`onClickPeca(Pos pos)`

Chamada sempre que uma casa é selecionada.

`onClickCasa(Pos pos)`
