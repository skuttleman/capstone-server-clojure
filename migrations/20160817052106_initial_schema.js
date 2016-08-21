exports.up = (knex, Promise) => {
  return Promise.all([
    knex.schema.createTable('players', players => {
      players.increments('id');
      players.string('social_id');
      players.string('image');
      players.string('email').unique();
      players.string('name');
      players.string('phone_number');
    }),
    knex.schema.createTable('game_statuses', gameStatuses => {
      gameStatuses.integer('id').unsigned().notNullable().primary();
      gameStatuses.string('status');
    })
  ]).then(() => {
    return knex.schema.createTable('game_levels', gameLevels => {
      gameLevels.increments('id');
      gameLevels.string('name');
      gameLevels.text('level_data');
      gameLevels.datetime('published_at').defaultTo(knex.fn.now());
      gameLevels.integer('creator_id').unsigned().references('id').inTable('players').onDelete('CASCADE');
    });
  }).then(() => {
    return knex.schema.createTable('games', games => {
      games.increments('id');
      games.integer('player1_id').unsigned().references('id').inTable('players').onDelete('CASCADE');
      games.integer('player2_id').unsigned().references('id').inTable('players').onDelete('CASCADE');
      games.datetime('last_updated').defaultTo(knex.fn.now());
      games.integer('creator_id').unsigned().references('id').inTable('players').onDelete('CASCADE');
      games.integer('game_level_id').unsigned().references('id').inTable('game_levels');
      games.integer('game_status_id').unsigned().references('id').inTable('game_statuses');
    });
  }).then(() => {
    return knex.schema.createTable('game_states', gameStates => {
      gameStates.increments('id');
      gameStates.integer('game_id').unsigned().references('id').inTable('games').onDelete('CASCADE');
      gameStates.datetime('time_stamp').defaultTo(knex.fn.now());
      gameStates.text('level_data');
    });
  }).then(() => {
    return knex.schema.createTable('game_wips', gameWips => {
      gameWips.increments('id');
      gameWips.integer('creator_id').unsigned().references('id').inTable('players').onDelete('CASCADE');
      gameWips.string('name');
      gameWips.text('level_data');
      gameWips.datetime('created_at').defaultTo(knex.fn.now());
      gameWips.datetime('updated_at').defaultTo(knex.fn.now());
      gameWips.integer('forked_level_id').unsigned().references('id').inTable('game_levels');
    });
  });
};

exports.down = (knex, Promise) => {
  let tables = ['game_wips', 'game_states', 'games', 'game_levels', 'players', 'game_statuses'];
  return tables.reduce((chain, table) => {
    return chain.then(() => knex.schema.dropTable(table));
  }, Promise.resolve());
};
