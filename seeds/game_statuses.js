exports.seed = (knex, Promise) => {
  return knex('game_statuses').del().then(() => {
    let statuses = ['not started', 'player1 turn', 'player2 turn', 'completed', 'rejected'];
    return Promise.all(statuses.map((status, i) => {
      return knex('game_statuses').insert({ id: i + 1, status });
    }));
  });
};
