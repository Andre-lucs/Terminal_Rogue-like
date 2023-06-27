# Um RPG rogue-like com cartas

Inspirado pelo jogo Rogue que foi o jogo que iniciou o que chamamos hoje em dia de jogos rogue-like.

>  Wikipedia: Roguelike ou rogue-like é um subgênero de jogos RPG, caracterizado pela geração de nível aleatória ou procedural durante a partida, mapa geralmente baseado em ladrilho e morte permanente.

A ideia para esse jogo é que as cartas sirvam como ações e jogabilidade principal do jogo.

Ele sera implementado ultilizando conceitos de jogos clássicos do genero RPG (ex. Final Fantasy) e Dungeon crawlers.

Sera feito em terminal como o rogue original.

### Requisitos do projeto

- [X] Variaveis de diferentes tipos e operaçoes lógicas;
  - Praticamente em qualquer arquivo;
- [X] Estruturas condicionais;
  - Praticamente em qualquer arquivo;
- [X] Estruturas de repetição;
  - Classes com logica mais complexa.Ex:Player,Game,GameMap;
- [X] Classes(...);
- [X] Entrada de dados por meio do metodo Game.HandlePlayerInput();
- [X] ArrayList e HashSet usados em uma serie de arquivos;
  - Ex:Player,GameMap;
- [X] Hierarquia de entidades;
- [X] Agregação de classes como as entidades no GameMap;
- [X] Classe abstrata Actor que representa uma entidade que possui acoes basicas que é extendido por Enemy e Player;
  - Classe abstrata não tem necessidade de nenhum metodo abstrato;
- [X] static final na classe TextColor;
- [X] Metodos sobrecarregados em algumas classes para facilitar o uso deles em casos especificos;
- [X] Sobreescrita no toString do Enemy e no metodo de criar card de defesa;
- [X] Interface Killable representando objetos que sao possiveis de atacar e matar;
  - Implementado por Player e Enemy.

> Status do Projeto: Concluido :heavy_check_mark:

## Diagrama de classes
Diagramas gerados automaticamente pelo InteliJ.
### Com dependencias
<img src="./Modelo de classes (Com Dependencias).png">

### Sem dependencias
<img src="./Modelo de classes (Sem Dependencias).png">

## Como executar
#### Linux
&rarr; O arquivo run.sh vai compilar todos as classes e em seguida executar o projeto. 
```bash
./run.sh
```
&rarr; Para apenas executar o projeto.
```bash
cd bin 
java Game
```