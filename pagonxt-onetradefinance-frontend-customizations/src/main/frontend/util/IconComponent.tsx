import * as React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { findIconDefinition, library } from '@fortawesome/fontawesome-svg-core';
import { far } from '@fortawesome/free-regular-svg-icons';
import { fas } from '@fortawesome/free-solid-svg-icons';

library.add(far);
library.add(fas);

const IconRegularComponent = ({ name }) => {
    return <FontAwesomeIcon icon={findIconDefinition({ prefix: 'far', iconName: name })} />;
};

const IconSolidComponent = ({ name }) => {
    return <FontAwesomeIcon icon={findIconDefinition({ prefix: 'fas', iconName: name })} />;
};

export { IconRegularComponent, IconSolidComponent };
