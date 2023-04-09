const path = require('path');

module.exports = {
  stories: ['../**/*.stories.@(js|md|tsx)'],
  addons: ['@storybook/addon-essentials', "@storybook/addon-links", "@storybook/addon-storysource", "@storybook/addon-cssresources"],

  // Here, we can add additional webpack configurations. Since we use scss to style our components, we need to add a rule.
  webpackFinal: async (config, { configType }) => {
    config.module.rules.push({
      test: /\.scss$/,
      use: ['style-loader', 'css-loader', 'sass-loader'],
      include: path.resolve(__dirname, '../')
    });

    // Return the altered config
    return config;
  }
}
