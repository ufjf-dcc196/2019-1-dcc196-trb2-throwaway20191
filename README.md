Felipe Inhan Matos

201335007

fmatos@ice.ufjf.br

Para esse projeto, foi utilizada apenas uma tabela e uma classe de modelo, *Tarefa*.
Todos os campos são Strings, exceto *dificuldade* e *Estado*.

O campo *Estado*, no modelo, é um *enum* que contém todos os estados possíveis e coordena e tradução entre os valores salvos no banco de dados com a interface da aplicação.

Foi decidido não utilizar campos *Date* na data de atualização e data limite pois não existe operação com data nos requisitos, além de o banco SQLite não suportar campos *date*.
