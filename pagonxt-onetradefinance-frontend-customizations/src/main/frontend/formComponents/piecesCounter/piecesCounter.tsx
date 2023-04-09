//@ts-nocheck
import * as React from 'react';
import { _, Model } from '@flowable/forms';
import './piecesCounter.css';

export function PiecesCounterComponent(props: Model.Props) {
    const { config } = props;
    const bem = _.bem('piecesCounter');

    if (config.value != 0 && !config.value) {
        return null;
    }

    return (
        <div className={bem()}>
            <span className={bem('value')}>{config.value}</span>
        </div>
    );
}
