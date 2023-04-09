import * as React from 'react';
import { _, Form } from '@flowable/forms';
import formComponents from '../formComponents';
import '@flowable/forms/flwforms.min.css';

const formProps = {
    debug: true,
    Components: formComponents,
};

type StorybookFormProps = {
    config?: any;
    sfs?: string;
    payload?: any;
};

export default function StorybookForm(props: StorybookFormProps) {
    return (
        <Form
            {...formProps}
            payload={props.payload}
            config={props.config ?? _.sfs.parse(props.sfs)}
        />
    );
}
