// TODO Understand how to update to docusaurus version 2.x
// See https://docusaurus.io/docs/site-config for all the possible
// site configuration options.

const siteConfig = {
    title: 'scala3mock',
    tagline: 'Native Scala 3 mocking system',
    url: 'https://francois.monniot.eu',
    baseUrl: '/scala3mock/',
    repoUrl: 'https://github.com/fmonniot/scala3mock',
    projectName: 'scala3mock',
    githubHost: 'github.com',
    // For top-level user or org sites, the organization is still the same.
    // e.g., for the https://JoelMarcey.github.io site, it would be set like...
    organizationName: 'fmonniot',
  
    // For no header links in the top nav bar -> headerLinks: [],
    headerLinks: [
      {doc: 'getting-started', label: 'Docs'},
      //{href: `/scala3mock/api/0.x/index.html`, label: 'API'},
      {href: 'https://github.com/fmonniot/scala3mock', label: "GitHub", external: true}
    ],
  
    /* path to images for header/footer */
    headerIcon: 'img/logo.svg',
    footerIcon: 'img/logo.svg',
    favicon: 'img/favicon.png',
  
    /* Colors for website */
    colors: {
      primaryColor: '#d36d6f',
      secondaryColor: '#294066',
    },
  
    // This copyright info is used in /core/Footer.js and blog RSS/Atom feeds.
    copyright: `Copyright (c) 2022-2023 François Monniot`,
  
    highlight: {
      // Highlight.js theme to use for syntax highlighting in code blocks.
      theme: 'github-gist',
      defaultLang: 'plaintext'
    },
  
    // Add custom scripts here that would be placed in <script> tags.
    scripts: ['https://buttons.github.io/buttons.js'],
  
    // On page navigation for the current documentation page.
    onPageNav: 'separate',
    // No .html extensions for paths.
    cleanUrl: true,
  
    // For sites with a sizable amount of content, set collapsible to true.
    // Expand/collapse the links and subcategories under categories.
    // docsSideNavCollapsible: true,
  
    // Show documentation's last contributor's name.
    enableUpdateBy: true,
  
    // Show documentation's last update time.
    enableUpdateTime: true,
    docsSideNavCollapsible: true,
  
    //separateCss: ['static/api'],
  };
  
  module.exports = siteConfig;