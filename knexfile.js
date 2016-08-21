module.exports = {
  development: {
    client: 'mysql',
    connection: {
      host: 'localhost',
      database: 'tilde_v2',
      user:     'root',
      password: 'root'
    }
  },
  production: {
    client: 'mysql',
    connection: process.env.DATABASE_URL
  }
};
