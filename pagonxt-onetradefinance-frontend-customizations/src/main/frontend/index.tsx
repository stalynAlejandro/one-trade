import formComponentMap from './formComponents';
import { additionalData } from './additionalData';
import './styles/index';
import './styles/general.scss';

const formComponents = [];
for (const formComponentName of Object.keys(formComponentMap)) {
    formComponents.push([formComponentName, formComponentMap[formComponentName]]);
}

export default {
    formComponents,
    additionalData,
};
