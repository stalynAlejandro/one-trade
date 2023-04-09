import { getContextTKeys } from '../getContextTKeys';

describe('Util getContextTKeys', () => {
  it('allows to send keys as an array of strings', () => {
    const result = getContextTKeys(
      ['testOne', 'testTwo'],
      'testPrecontext.testContext',
    );

    expect(result).toEqual([
      'testPrecontext.testContext.testOne',
      'testPrecontext.testOne',
      'testOne',
      'testPrecontext.testContext.testTwo',
      'testPrecontext.testTwo',
      'testTwo',
    ]);
  });

  it('allows to send keys as a string', () => {
    const result = getContextTKeys('testOne', 'testPrecontext.testContext');

    expect(result).toEqual([
      'testPrecontext.testContext.testOne',
      'testPrecontext.testOne',
      'testOne',
    ]);
  });

  it('returns all contexts from more specific to less specific', () => {
    const result = getContextTKeys('testOne', 'testPrecontext.testContext');

    expect(result).toEqual([
      'testPrecontext.testContext.testOne',
      'testPrecontext.testOne',
      'testOne',
    ]);
  });

  it('works with single context prefix', () => {
    const result = getContextTKeys('testOne', 'testPrecontext');

    expect(result).toEqual(['testPrecontext.testOne', 'testOne']);
  });

  it('works with multiple context prefixes', () => {
    const result = getContextTKeys('testOne', 'testPrecontext.testContext');

    expect(result).toEqual([
      'testPrecontext.testContext.testOne',
      'testPrecontext.testOne',
      'testOne',
    ]);
  });
});
