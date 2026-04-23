-- São Paulo
INSERT INTO cities (name, slug, state_id) SELECT 'São Paulo', 'sao-paulo', id FROM states WHERE uf = 'SP';
INSERT INTO cities (name, slug, state_id) SELECT 'Campinas', 'campinas', id FROM states WHERE uf = 'SP';
INSERT INTO cities (name, slug, state_id) SELECT 'Guarulhos', 'guarulhos', id FROM states WHERE uf = 'SP';
INSERT INTO cities (name, slug, state_id) SELECT 'Santo André', 'santo-andre', id FROM states WHERE uf = 'SP';
INSERT INTO cities (name, slug, state_id) SELECT 'Osasco', 'osasco', id FROM states WHERE uf = 'SP';
INSERT INTO cities (name, slug, state_id) SELECT 'São Bernardo do Campo', 'sao-bernardo-do-campo', id FROM states WHERE uf = 'SP';

-- Rio de Janeiro
INSERT INTO cities (name, slug, state_id) SELECT 'Rio de Janeiro', 'rio-de-janeiro', id FROM states WHERE uf = 'RJ';
INSERT INTO cities (name, slug, state_id) SELECT 'Niterói', 'niteroi', id FROM states WHERE uf = 'RJ';
INSERT INTO cities (name, slug, state_id) SELECT 'Nova Iguaçu', 'nova-iguacu', id FROM states WHERE uf = 'RJ';
INSERT INTO cities (name, slug, state_id) SELECT 'Duque de Caxias', 'duque-de-caxias', id FROM states WHERE uf = 'RJ';

-- Minas Gerais
INSERT INTO cities (name, slug, state_id) SELECT 'Belo Horizonte', 'belo-horizonte', id FROM states WHERE uf = 'MG';
INSERT INTO cities (name, slug, state_id) SELECT 'Uberlândia', 'uberlandia', id FROM states WHERE uf = 'MG';
INSERT INTO cities (name, slug, state_id) SELECT 'Contagem', 'contagem', id FROM states WHERE uf = 'MG';
INSERT INTO cities (name, slug, state_id) SELECT 'Juiz de Fora', 'juiz-de-fora', id FROM states WHERE uf = 'MG';

-- Distrito Federal
INSERT INTO cities (name, slug, state_id) SELECT 'Brasília', 'brasilia', id FROM states WHERE uf = 'DF';
INSERT INTO cities (name, slug, state_id) SELECT 'Ceilândia', 'ceilandia', id FROM states WHERE uf = 'DF';
INSERT INTO cities (name, slug, state_id) SELECT 'Taguatinga', 'taguatinga', id FROM states WHERE uf = 'DF';

-- Goiás
INSERT INTO cities (name, slug, state_id) SELECT 'Goiânia', 'goiania', id FROM states WHERE uf = 'GO';
INSERT INTO cities (name, slug, state_id) SELECT 'Aparecida de Goiânia', 'aparecida-de-goiania', id FROM states WHERE uf = 'GO';
INSERT INTO cities (name, slug, state_id) SELECT 'Anápolis', 'anapolis', id FROM states WHERE uf = 'GO';

-- Paraná
INSERT INTO cities (name, slug, state_id) SELECT 'Curitiba', 'curitiba', id FROM states WHERE uf = 'PR';
INSERT INTO cities (name, slug, state_id) SELECT 'Londrina', 'londrina', id FROM states WHERE uf = 'PR';
INSERT INTO cities (name, slug, state_id) SELECT 'Maringá', 'maringa', id FROM states WHERE uf = 'PR';

-- Rio Grande do Sul
INSERT INTO cities (name, slug, state_id) SELECT 'Porto Alegre', 'porto-alegre', id FROM states WHERE uf = 'RS';
INSERT INTO cities (name, slug, state_id) SELECT 'Caxias do Sul', 'caxias-do-sul', id FROM states WHERE uf = 'RS';
INSERT INTO cities (name, slug, state_id) SELECT 'Pelotas', 'pelotas', id FROM states WHERE uf = 'RS';

-- Bahia
INSERT INTO cities (name, slug, state_id) SELECT 'Salvador', 'salvador', id FROM states WHERE uf = 'BA';
INSERT INTO cities (name, slug, state_id) SELECT 'Feira de Santana', 'feira-de-santana', id FROM states WHERE uf = 'BA';
INSERT INTO cities (name, slug, state_id) SELECT 'Vitória da Conquista', 'vitoria-da-conquista', id FROM states WHERE uf = 'BA';

-- Pernambuco
INSERT INTO cities (name, slug, state_id) SELECT 'Recife', 'recife', id FROM states WHERE uf = 'PE';
INSERT INTO cities (name, slug, state_id) SELECT 'Caruaru', 'caruaru', id FROM states WHERE uf = 'PE';
INSERT INTO cities (name, slug, state_id) SELECT 'Olinda', 'olinda', id FROM states WHERE uf = 'PE';

-- Ceará
INSERT INTO cities (name, slug, state_id) SELECT 'Fortaleza', 'fortaleza', id FROM states WHERE uf = 'CE';
INSERT INTO cities (name, slug, state_id) SELECT 'Caucaia', 'caucaia', id FROM states WHERE uf = 'CE';
INSERT INTO cities (name, slug, state_id) SELECT 'Juazeiro do Norte', 'juazeiro-do-norte', id FROM states WHERE uf = 'CE';

-- Santa Catarina
INSERT INTO cities (name, slug, state_id) SELECT 'Florianópolis', 'florianopolis', id FROM states WHERE uf = 'SC';
INSERT INTO cities (name, slug, state_id) SELECT 'Joinville', 'joinville', id FROM states WHERE uf = 'SC';
INSERT INTO cities (name, slug, state_id) SELECT 'Blumenau', 'blumenau', id FROM states WHERE uf = 'SC';

-- Espírito Santo
INSERT INTO cities (name, slug, state_id) SELECT 'Vitória', 'vitoria', id FROM states WHERE uf = 'ES';
INSERT INTO cities (name, slug, state_id) SELECT 'Vila Velha', 'vila-velha', id FROM states WHERE uf = 'ES';
INSERT INTO cities (name, slug, state_id) SELECT 'Serra', 'serra', id FROM states WHERE uf = 'ES';

-- Amazonas
INSERT INTO cities (name, slug, state_id) SELECT 'Manaus', 'manaus', id FROM states WHERE uf = 'AM';

-- Pará
INSERT INTO cities (name, slug, state_id) SELECT 'Belém', 'belem', id FROM states WHERE uf = 'PA';
INSERT INTO cities (name, slug, state_id) SELECT 'Ananindeua', 'ananindeua', id FROM states WHERE uf = 'PA';

-- Capitais dos demais estados
INSERT INTO cities (name, slug, state_id) SELECT 'Rio Branco', 'rio-branco', id FROM states WHERE uf = 'AC';
INSERT INTO cities (name, slug, state_id) SELECT 'Maceió', 'maceio', id FROM states WHERE uf = 'AL';
INSERT INTO cities (name, slug, state_id) SELECT 'Macapá', 'macapa', id FROM states WHERE uf = 'AP';
INSERT INTO cities (name, slug, state_id) SELECT 'São Luís', 'sao-luis', id FROM states WHERE uf = 'MA';
INSERT INTO cities (name, slug, state_id) SELECT 'Cuiabá', 'cuiaba', id FROM states WHERE uf = 'MT';
INSERT INTO cities (name, slug, state_id) SELECT 'Campo Grande', 'campo-grande', id FROM states WHERE uf = 'MS';
INSERT INTO cities (name, slug, state_id) SELECT 'João Pessoa', 'joao-pessoa', id FROM states WHERE uf = 'PB';
INSERT INTO cities (name, slug, state_id) SELECT 'Teresina', 'teresina', id FROM states WHERE uf = 'PI';
INSERT INTO cities (name, slug, state_id) SELECT 'Natal', 'natal', id FROM states WHERE uf = 'RN';
INSERT INTO cities (name, slug, state_id) SELECT 'Porto Velho', 'porto-velho', id FROM states WHERE uf = 'RO';
INSERT INTO cities (name, slug, state_id) SELECT 'Boa Vista', 'boa-vista', id FROM states WHERE uf = 'RR';
INSERT INTO cities (name, slug, state_id) SELECT 'Aracaju', 'aracaju', id FROM states WHERE uf = 'SE';
INSERT INTO cities (name, slug, state_id) SELECT 'Palmas', 'palmas', id FROM states WHERE uf = 'TO';
