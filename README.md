# ES2_damas
Jogo de damas.

Trabalho da disciplina de Engenharia de Software 2, turma de 2016.2.

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

`onPieceMoved();`

Chamada sempre depois que o jogo termina.

`onGameFinished();`

Chamada sempre que uma peça é removida do tabuleiro.

`onPieceRemoved();`

Chamada sempre que uma peça vira dama.

`virouDama();`
